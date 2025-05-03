package org.fourthperson.data.repository;

import org.fourthperson.domain.entity.Evaluation;
import org.fourthperson.domain.entity.EvaluationArgs;
import org.fourthperson.domain.repository.EvaluationRepo;

public class EvaluationRepoImpl extends EvaluationRepo {
    @Override
    public Evaluation getEvaluation(EvaluationArgs args) {
        try {
            int introCount = 0, extroCount = 0;
            String[] strings = args.getAnswers().replace(" ", "").split(";");
            for (int i = 0; i < args.getAnswerCount(); i++) {
                String text = strings[i];
                if (!text.equalsIgnoreCase("true") && !text.equalsIgnoreCase("false")) {
                    return null;
                }
                if (Boolean.parseBoolean(text)) {
                    introCount++;
                } else {
                    extroCount++;
                }
            }
            String e = introCount > extroCount ? "Introverted" : extroCount > introCount ? "Extroverted" : "Balanced";
            return new Evaluation(e);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
