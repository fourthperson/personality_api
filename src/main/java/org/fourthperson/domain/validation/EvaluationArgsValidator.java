package org.fourthperson.domain.validation;

import com.google.inject.Singleton;
import org.fourthperson.domain.entity.EvaluationArgs;
import org.fourthperson.domain.exception.InvalidInputException;

import java.util.ArrayList;
import java.util.List;

@Singleton
public class EvaluationArgsValidator {

    public void validate(EvaluationArgs args) {
        List<String> errors = new ArrayList<>();

        if (args == null) {
            errors.add("Request body cannot be empty.");
        } else {
            if (args.answers() == null || args.answers().trim().isEmpty()) {
                errors.add("Answers cannot be empty.");
            }

            if (args.answer_count() <= 0) {
                errors.add("Answer count must be positive.");
            }

            if (args.answers() != null && !args.answers().trim().isEmpty() && args.answer_count() > 0) {
                String[] answerTokens = args.answers().replace(" ", "").split(";");
                if (answerTokens.length != args.answer_count()) {
                    errors.add("Number of answers provided (" + answerTokens.length + ") does not match expected answer_count (" + args.answer_count() + ").");
                } else {
                    for (String token : answerTokens) {
                        if (!"true".equalsIgnoreCase(token) && !"false".equalsIgnoreCase(token)) {
                            errors.add("Invalid answer format. Each answer must be 'true' or 'false'. Found: '" + token + "'.");
                            break;
                        }
                    }
                }
            }
        }

        if (!errors.isEmpty()) {
            throw new InvalidInputException(String.join("; ", errors));
        }
    }
}