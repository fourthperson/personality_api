package org.fourthperson.data.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "question")
public class DbQuestion {
    @DatabaseField(columnName = "id", generatedId = true)
    private int id;
    @DatabaseField(columnName = "text")
    private String text;
    @DatabaseField(columnName = "created_on")
    private String created_on;

    public DbQuestion() {
    }

    public int getId() {
        return id;
    }

    public String getText() {
        return text;
    }
}
