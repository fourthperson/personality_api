package org.fourthperson.presentation.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.javalin.config.JavalinConfig;
import org.fourthperson.Config;
import org.fourthperson.domain.entity.AppResponse;
import org.fourthperson.domain.entity.Evaluation;
import org.fourthperson.domain.entity.EvaluationArgs;
import org.fourthperson.domain.exception.DataAccessException;
import org.fourthperson.domain.use_case.GetEvaluationUseCase;
import org.fourthperson.domain.validation.EvaluationArgsValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
public class EvaluationController {
    private final Logger logger = LoggerFactory.getLogger(EvaluationController.class);
    private final GetEvaluationUseCase getEvaluationUseCase;
    private final ObjectMapper objectMapper;
    private final EvaluationArgsValidator evaluationArgsValidator;

    @Inject
    public EvaluationController(
            GetEvaluationUseCase getEvaluationUseCase,
            ObjectMapper objectMapper,
            EvaluationArgsValidator evaluationArgsValidator
    ) {
        this.getEvaluationUseCase = getEvaluationUseCase;
        this.objectMapper = objectMapper;
        this.evaluationArgsValidator = evaluationArgsValidator;
    }

    public void registerRoutes(JavalinConfig config) {
        config.routes.post("/evaluate", context -> {
            final EvaluationArgs eArgs = objectMapper.readValue(context.body(), EvaluationArgs.class);

            evaluationArgsValidator.validate(eArgs);

            final Evaluation evaluation = getEvaluationUseCase.invoke(eArgs);

            if (evaluation == null || evaluation.outcome() == null) {
                throw new DataAccessException(Config.evaluationError);
            }

            context.json(AppResponse.success(evaluation.outcome())).status(200);
            logger.info("Evaluation completed successfully!");
        });
    }
}