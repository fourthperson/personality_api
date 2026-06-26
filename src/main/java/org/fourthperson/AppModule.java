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
import org.fourthperson.domain.exception.DataAccessException;
import org.fourthperson.domain.exception.InvalidInputException;
import org.fourthperson.domain.repository.EvaluationRepo;
import org.fourthperson.domain.repository.QuestionRepo;
import org.fourthperson.domain.use_case.GetEvaluationUseCase;
import org.fourthperson.domain.use_case.GetQuestionUseCase;
import org.fourthperson.presentation.controller.EvaluationController;
import org.fourthperson.presentation.controller.QuestionController;
import org.fourthperson.domain.validation.EvaluationArgsValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;

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

        // Bind the controllers
        bind(QuestionController.class).in(Singleton.class);
        bind(EvaluationController.class).in(Singleton.class);
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
            QuestionController questionController,
            EvaluationController evaluationController,
            JdbcPooledConnectionSource connectionSource
    ) {
        return Javalin.create(config -> {
            config.jetty.port = Config.serverPort;
            config.router.ignoreTrailingSlashes = true;

            // Centralized Exception Handling
            config.routes.exception(InvalidInputException.class, (e, ctx) -> {
                logger.warn("Invalid input received: {}", e.getMessage());
                ctx.json(AppResponse.error(e.getMessage())).status(400); // 400 Bad Request
            });

            config.routes.exception(DataAccessException.class, (e, ctx) -> {
                logger.error("Data access error: {}", e.getMessage(), e);
                ctx.json(AppResponse.error(Config.internalServerError)).status(500); // 500 Internal Server Error
            });

            config.routes.exception(Exception.class, (e, ctx) -> {
                logger.error("Unhandled exception in route: {}", ctx.path(), e);
                ctx.json(AppResponse.error("An unexpected error occurred: " + e.getMessage())).status(500); // Generic 500
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

            // Register routes from controllers
            questionController.registerRoutes(config);
            evaluationController.registerRoutes(config);

            // Base route
            config.routes.get("/", context -> context.status(403));
        });
    }
}