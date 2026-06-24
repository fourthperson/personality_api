package org.fourthperson.domain.use_case;

import com.google.inject.Inject;
import org.fourthperson.domain.entity.Question;
import org.fourthperson.domain.repository.QuestionRepo;

import java.util.List;

public class GetQuestionUseCase {
    final QuestionRepo questionRepo;

    @Inject
    public GetQuestionUseCase(QuestionRepo questionRepo) {
        this.questionRepo = questionRepo;
    }

    public List<Question> invoke() {
        return questionRepo.getQuestions();
    }
}