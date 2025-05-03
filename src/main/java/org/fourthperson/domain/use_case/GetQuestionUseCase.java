package org.fourthperson.domain.use_case;

import org.fourthperson.domain.entity.Question;
import org.fourthperson.domain.repository.QuestionRepo;

import java.util.List;

public class GetQuestionUseCase {
    final QuestionRepo questionRepo;

    public GetQuestionUseCase(QuestionRepo questionRepo) {
        this.questionRepo = questionRepo;
    }

    public List<Question> invoke() {
        return questionRepo.getQuestions();
    }
}
