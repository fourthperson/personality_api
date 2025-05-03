package org.fourthperson.data.source;

import org.fourthperson.data.entity.DbQuestion;

import java.util.List;

public abstract class DbDataSource {
    public abstract List<DbQuestion> getDbQuestions();
}
