package org.fourthperson.presentation.controller;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.javalin.config.JavalinConfig;
import org.fourthperson.domain.entity.AppResponse;
import org.fourthperson.domain.entity.Question;
import org.fourthperson.domain.use_case.GetQuestionUseCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Singleton
public class QuestionController {
    private final Logger logger = LoggerFactory.getLogger(QuestionController.class);
    private final GetQuestionUseCase getQuestionUseCase;

    @Inject
    public QuestionController(GetQuestionUseCase getQuestionUseCase) {
        this.getQuestionUseCase = getQuestionUseCase;
    }

    public void registerRoutes(JavalinConfig config) {
        config.routes.get("/questions", context -> {
            final List<Question> questions = getQuestionUseCase.invoke();
            context.json(AppResponse.success(questions)).status(200);
            logger.info("Questions retrieved successfully!");
        });
    }
}