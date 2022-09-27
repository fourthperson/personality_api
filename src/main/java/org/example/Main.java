package org.example;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.table.DatabaseTable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static spark.Spark.*;

public class Main {
    private static final String DB_NAME = "pers_test";
    private static final String DB_USER = "dev";
    private static final String DB_PASS = "I@N2131";

    private static final String CONTENT_TYPE = "application/json";

    private static final Gson gson = new Gson();

    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
//        String databaseUrl = "jdbc:mysql://localhost/" + DB_NAME;
        String databaseUrl = "jdbc:mariadb://localhost:3306/" + DB_NAME;

        Dao<Question, String> questionDao;

        try {
            JdbcConnectionSource connectionSource = new JdbcConnectionSource(databaseUrl);
            connectionSource.setUsername(DB_USER);
            connectionSource.setPassword(DB_PASS);

            questionDao = DaoManager.createDao(connectionSource, Question.class);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            throw new RuntimeException(e);
        }

        port(3030);

        get("questions", (request, response) -> {
            response.type(CONTENT_TYPE);
            response.body(getQuestions(questionDao));
            return response.body();
        });

        post("mark", (request, response) -> {
            response.type(CONTENT_TYPE);
            response.body(mark(response.body()));
            return response.body();
        });
    }

    private static String getQuestions(Dao<Question, String> dao) {
        try {
            List<Question> questions = dao.queryForAll();

            AppResponse response = new AppResponse();
            response.setStatus(200);
            response.setData(questions);

            return gson.toJson(response);
        } catch (Exception e) {
            return exception(e);
        }
    }

    private static String mark(String json) {
        try {
            MarkRequest incoming = gson.fromJson(json, MarkRequest.class);

            int introvertCount = 0, extrovertCount = 0;
            String[] strings = incoming.getAnswers().split(";");
            for (int i = 0; i < incoming.getAnswerCount(); i++) {
                if (Boolean.parseBoolean(strings[i])) {
                    introvertCount++;
                } else {
                    extrovertCount++;
                }
            }

            String outcome;
            if (introvertCount > extrovertCount) {
                outcome = "Introverted";
            } else if (extrovertCount > introvertCount) {
                outcome = "Extroverted";
            } else {
                outcome = "Balanced";
            }

            MarkResponse response = new MarkResponse();
            response.setStatus(200);
            response.setData(outcome);

            return gson.toJson(response);
        } catch (Exception e) {
            return exception(e);
        }
    }

    private static String exception(Exception e) {
        LOGGER.error(e.getMessage());
        ErrorResponse response = new ErrorResponse();
        response.setStatus(500);
        response.setError(e.getMessage());
        return gson.toJson(response);
    }
}

class AppResponse {
    @SerializedName("status")
    @Expose
    private int status;

    @SerializedName("data")
    @Expose
    private Object data;

    public void setStatus(int status) {
        this.status = status;
    }

    public void setData(Object data) {
        this.data = data;
    }
}

class ErrorResponse extends AppResponse {
    @SerializedName("error")
    @Expose
    private String error;

    public void setError(String error) {
        this.error = error;
    }
}

class MarkRequest {
    @SerializedName("answer_count")
    @Expose
    private Integer answerCount;
    @SerializedName("answers")
    @Expose
    private String answers;

    public Integer getAnswerCount() {
        return answerCount;
    }

    public String getAnswers() {
        return answers;
    }
}

class MarkResponse extends AppResponse {
}

@DatabaseTable(tableName = "question")
class Question {
    @DatabaseField(columnName = "id", generatedId = true)
    private int id;
    @DatabaseField(columnName = "text")
    private String text;
    @DatabaseField(columnName = "created_on")
    private String created_on;

    public Question() {
    }
}