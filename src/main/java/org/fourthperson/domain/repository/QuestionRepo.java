package org.fourthperson.domain.repository;

import org.fourthperson.domain.entity.Question;

import java.util.List;

public abstract class QuestionRepo {
    public abstract List<Question> getQuestions();
}
