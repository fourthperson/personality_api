package org.fourthperson;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.fourthperson.domain.repository.EvaluationRepo;
import org.fourthperson.domain.repository.QuestionRepo;
import org.fourthperson.domain.use_case.GetEvaluationUseCase;
import org.fourthperson.domain.use_case.GetQuestionUseCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        // Database DAOs
        Dao<DbQuestion, String> questionDao;
        try {
            JdbcPooledConnectionSource connectionSource = new JdbcPooledConnectionSource(Config.databaseUrl);
            connectionSource.setUsername(Config.dbUser);
            connectionSource.setPassword(Config.dbPass);

            questionDao = DaoManager.createDao(connectionSource, DbQuestion.class);
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }

        // Data Sources
        final DbDataSource dbDataSource = new DbDataSourceImpl(questionDao);
        // Repositories
        final QuestionRepo questionRepo = new QuestionRepoImpl(dbDataSource);
        final EvaluationRepo evaluationRepo = new EvaluationRepoImpl();
        // Use-Cases
        final GetQuestionUseCase getQuestionUseCase = new GetQuestionUseCase(questionRepo);
        final GetEvaluationUseCase getEvaluationUseCase = new GetEvaluationUseCase(evaluationRepo);

        // Server app
        final Javalin app = Javalin.create();

        // Server routes
        app.get("/", context -> context.status(403));

        app.get("/questions", context -> {
            List<Question> questions = getQuestionUseCase.invoke();
            boolean successful = questions != null && !questions.isEmpty();
            AppResponse resp = AppResponse.create(successful ? 200 : 500, successful ? questions : "An error occurred");
            context.json(resp);
        });

        app.post("/evaluate", context -> {
            EvaluationArgs eArgs = new ObjectMapper().readValue(context.body(), EvaluationArgs.class);
            Evaluation evaluation = getEvaluationUseCase.invoke(eArgs);
            boolean successful = evaluation.outcome != null;
            String data = successful ? evaluation.outcome : Config.evaluationError;
            AppResponse resp = AppResponse.create(successful ? 200 : 500, data);
            context.json(resp);
        });

        app.get("/questions/", context -> context.redirect("/questions"));

        app.get("/evaluate/", context -> context.redirect("/evaluate"));

        // Start server
        app.start(Config.serverPort);
    }
}