package org.fourthperson;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcPooledConnectionSource;
import io.javalin.Javalin;
import org.fourthperson.data.entity.DbQuestion;
import org.fourthperson.data.repository.EvaluationRepoImpl;
import org.fourthperson.data.repository.QuestionRepoImpl;
import org.fourthperson.data.source.DbDataSource;
import org.fourthperson.data.source.DbDataSourceImpl;
import org.fourthperson.domain.entity.AppResponse;
import org.fourthperson.domain.entity.Evaluation;
import org.fourthperson.domain.entity.EvaluationArgs;
import org.fourthperson.domain.entity.Question;
import org.fourthperson.domain.exception.DataAccessException;
import org.fourthperson.domain.exception.InvalidInputException;
import org.fourthperson.domain.repository.EvaluationRepo;
import org.fourthperson.domain.repository.QuestionRepo;
import org.fourthperson.domain.use_case.GetEvaluationUseCase;
import org.fourthperson.domain.use_case.GetQuestionUseCase;
import org.fourthperson.domain.validation.EvaluationArgsValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.List;

public class AppModule extends AbstractModule {

    private static final Logger logger = LoggerFactory.getLogger(AppModule.class);

    @Override
    protected void configure() {
        // Bind interfaces to their implementations
        bind(DbDataSource.class).to(DbDataSourceImpl.class).in(Singleton.class);
        bind(QuestionRepo.class).to(QuestionRepoImpl.class).in(Singleton.class);
        bind(EvaluationRepo.class).to(EvaluationRepoImpl.class).in(Singleton.class);

        // Use-cases can be bound directly as they typically depend on interfaces
        bind(GetQuestionUseCase.class).in(Singleton.class);
        bind(GetEvaluationUseCase.class).in(Singleton.class);

        // Jackson ObjectMapper for JSON processing
        bind(ObjectMapper.class).in(Singleton.class);

        // Bind the validator
        bind(EvaluationArgsValidator.class).in(Singleton.class);
    }

    @Provides
    @Singleton
    JdbcPooledConnectionSource provideConnectionSource() {
        try {
            final JdbcPooledConnectionSource connectionSource = new JdbcPooledConnectionSource(Config.databaseUrl);
            connectionSource.setUsername(Config.dbUser);
            connectionSource.setPassword(Config.dbPass);
            return connectionSource;
        } catch (SQLException e) {
            logger.error("Failed to create database connection source", e);
            throw new RuntimeException("Failed to create database connection source", e);
        }
    }

    @Provides
    @Singleton
    Dao<DbQuestion, String> provideQuestionDao(JdbcPooledConnectionSource connectionSource) {
        try {
            return DaoManager.createDao(connectionSource, DbQuestion.class);
        } catch (SQLException e) {
            logger.error("Failed to create DbQuestion DAO", e);
            throw new RuntimeException("Failed to create DbQuestion DAO", e);
        }
    }

    @Provides
    @Singleton
    Javalin provideJavalin(
            GetQuestionUseCase getQuestionUseCase,
            GetEvaluationUseCase getEvaluationUseCase,
            ObjectMapper objectMapper,
            JdbcPooledConnectionSource connectionSource,
            EvaluationArgsValidator evaluationArgsValidator
    ) {
        return Javalin.create(config -> {
            config.jetty.port = Config.serverPort;
            config.router.ignoreTrailingSlashes = true;

            // Centralized Exception Handling
            config.routes.exception(InvalidInputException.class, (e, ctx) -> {
                logger.warn("Invalid input received: {}", e.getMessage());
                ctx.json(AppResponse.error(e.getMessage())).status(400);
            });

            config.routes.exception(DataAccessException.class, (e, ctx) -> {
                logger.error("Data access error: {}", e.getMessage(), e);
                ctx.json(AppResponse.error(Config.internalServerError)).status(500);
            });

            config.routes.exception(Exception.class, (e, ctx) -> {
                logger.error("Unhandled exception in route: {}", ctx.path(), e);
                ctx.json(AppResponse.error("An unexpected error occurred: " + e.getMessage())).status(500);
            });

            // Graceful Database Connection Shutdown
            config.events.serverStopped(() -> {
                try {
                    connectionSource.close();
                    logger.info("Database connection source closed successfully.");
                } catch (Exception e) {
                    logger.error("Error closing database connection source", e);
                }
            });

            config.routes.get("/", context -> context.status(403));

            config.routes.get("/questions", context -> {
                List<Question> questions = getQuestionUseCase.invoke();
                context.json(AppResponse.success(questions)).status(200);
            });

            config.routes.post("/evaluate", context -> {
                final EvaluationArgs eArgs = objectMapper.readValue(context.body(), EvaluationArgs.class);

                evaluationArgsValidator.validate(eArgs);

                final Evaluation evaluation = getEvaluationUseCase.invoke(eArgs);

                if (evaluation == null || evaluation.outcome() == null) {
                    throw new DataAccessException(Config.evaluationError);
                }

                context.json(AppResponse.success(evaluation.outcome())).status(200);
            });
        });
    }
}