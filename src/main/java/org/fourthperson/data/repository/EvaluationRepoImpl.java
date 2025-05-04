package org.fourthperson.data.repository;

import org.fourthperson.domain.entity.Evaluation;
import org.fourthperson.domain.entity.EvaluationArgs;
import org.fourthperson.domain.repository.EvaluationRepo;

import java.util.logging.Level;
import java.util.logging.Logger;

public class EvaluationRepoImpl extends EvaluationRepo {
    final Logger logger = Logger.getLogger(EvaluationRepoImpl.class.getCanonicalName());

    @Override
    public Evaluation getEvaluation(EvaluationArgs args) {
        try {
            int iCount = 0, eCount = 0;
            String[] strings = args.answers().replace(" ", "").split(";");
            for (int i = 0; i < args.answer_count(); i++) {
                String text = strings[i];
                if (!text.equalsIgnoreCase("true") && !text.equalsIgnoreCase("false")) {
                    return null;
                }
                if (Boolean.parseBoolean(text)) {
                    iCount++;
                } else {
                    eCount++;
                }
            }
            String e = iCount > eCount ? "Introverted" : eCount > iCount ? "Extroverted" : "Balanced";
            return new Evaluation(e);
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage());
        }
        return null;
    }
}
