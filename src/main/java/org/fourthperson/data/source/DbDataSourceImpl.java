package org.fourthperson.data.source;

import com.j256.ormlite.dao.Dao;
import org.fourthperson.data.entity.DbQuestion;

import java.util.Collections;
import java.util.List;

public class DbDataSourceImpl extends DbDataSource {
    final Dao<DbQuestion, String> questionDao;

    public DbDataSourceImpl(Dao<DbQuestion, String> questionDao) {
        this.questionDao = questionDao;
    }

    @Override
    public List<DbQuestion> getDbQuestions() {
        try {
            List<DbQuestion> questions = questionDao.queryForAll();
            Collections.shuffle(questions);
            return questions;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
