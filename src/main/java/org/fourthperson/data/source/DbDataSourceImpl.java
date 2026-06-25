package org.fourthperson.data.source;

import com.google.inject.Inject;
import com.j256.ormlite.dao.Dao;
import org.fourthperson.data.entity.DbQuestion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;

public class DbDataSourceImpl extends DbDataSource {
    final Dao<DbQuestion, String> questionDao;
    final Logger logger = LoggerFactory.getLogger(DbDataSourceImpl.class);

    @Inject
    public DbDataSourceImpl(Dao<DbQuestion, String> questionDao) {
        this.questionDao = questionDao;
    }

    @Override
    public List<DbQuestion> getDbQuestions() {
        try {
            final List<DbQuestion> questions = questionDao.queryForAll();
            Collections.shuffle(questions);
            return questions;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }
}