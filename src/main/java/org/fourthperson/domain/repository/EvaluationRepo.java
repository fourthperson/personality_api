package org.fourthperson.domain.repository;

import org.fourthperson.domain.entity.Evaluation;
import org.fourthperson.domain.entity.EvaluationArgs;

public abstract class EvaluationRepo {
    public abstract Evaluation getEvaluation(EvaluationArgs args);
}
