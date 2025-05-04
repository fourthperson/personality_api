package org.fourthperson.data.source;

import com.j256.ormlite.dao.Dao;
import org.fourthperson.data.entity.DbQuestion;

import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DbDataSourceImpl extends DbDataSource {
    final Dao<DbQuestion, String> questionDao;
    final Logger logger = Logger.getLogger(DbDataSourceImpl.class.getCanonicalName());

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
            logger.log(Level.SEVERE, e.getMessage());
        }
        return null;
    }
}
