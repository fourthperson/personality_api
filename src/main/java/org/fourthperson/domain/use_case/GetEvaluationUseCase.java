package org.fourthperson.domain.use_case;

import com.google.inject.Inject;
import org.fourthperson.domain.entity.Evaluation;
import org.fourthperson.domain.entity.EvaluationArgs;
import org.fourthperson.domain.repository.EvaluationRepo;

public class GetEvaluationUseCase {
    final EvaluationRepo evaluationRepo;

    @Inject
    public GetEvaluationUseCase(EvaluationRepo evaluationRepo) {
        this.evaluationRepo = evaluationRepo;
    }

    public Evaluation invoke(EvaluationArgs args) {
        return evaluationRepo.getEvaluation(args);
    }
}