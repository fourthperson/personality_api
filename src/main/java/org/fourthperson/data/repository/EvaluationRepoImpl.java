package org.fourthperson.data.repository;

import com.google.inject.Inject;
import org.fourthperson.domain.entity.Evaluation;
import org.fourthperson.domain.entity.EvaluationArgs;
import org.fourthperson.domain.repository.EvaluationRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EvaluationRepoImpl extends EvaluationRepo {
    final Logger logger = LoggerFactory.getLogger(EvaluationRepoImpl.class);

    @Inject
    public EvaluationRepoImpl() {
    }

    @Override
    public Evaluation getEvaluation(EvaluationArgs args) {
        try {
            int iCount = 0, eCount = 0;
            final String[] strings = args.answers().replace(" ", "").split(";");

            for (int i = 0; i < args.answer_count(); i++) {
                final String text = strings[i];
                if (Boolean.parseBoolean(text)) {
                    iCount++;
                } else {
                    eCount++;
                }
            }
            final String e = iCount > eCount ? "Introverted" : eCount > iCount ? "Extroverted" : "Balanced";
            return new Evaluation(e);
        } catch (Exception e) {
            logger.error("Error during evaluation processing", e);
            throw new RuntimeException("An unexpected error occurred during evaluation", e);
        }
    }
}