package org.fourthperson.data.repository;

import com.google.inject.Inject;
import org.fourthperson.data.entity.DbQuestion;
import org.fourthperson.data.source.DbDataSource;
import org.fourthperson.domain.entity.Question;
import org.fourthperson.domain.repository.QuestionRepo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class QuestionRepoImpl extends QuestionRepo {
    final DbDataSource dbDataSource;

    @Inject
    public QuestionRepoImpl(DbDataSource dbDataSource) {
        this.dbDataSource = dbDataSource;
    }

    @Override
    public List<Question> getQuestions() {
        final List<DbQuestion> dbQuestions = dbDataSource.getDbQuestions();

        if (dbQuestions.isEmpty()) {
            return Collections.emptyList();
        }

        final ArrayList<Question> questions = new ArrayList<>();
        for (DbQuestion dbQuestion : dbQuestions) {
            Question question = new Question(dbQuestion.getId(), dbQuestion.getText());
            questions.add(question);
        }
        return questions;
    }
}