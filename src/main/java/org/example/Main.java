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

        get("/questions", (request, response) -> {
            response.type(CONTENT_TYPE);
            response.body(getQuestions(questionDao));
            return response.body();
        });

        post("/evaluate", (request, response) -> {
            response.type(CONTENT_TYPE);
            response.body(mark(request.body()));
            return response.body();
        });
    }

    private static String getQuestions(Dao<Question, String> dao) {
        try {
            List<Question> questions = dao.queryForAll();
            return response(200, questions).toJson();
        } catch (Exception e) {
            return exception(e);
        }
    }

    private static String mark(String json) {
        try {
            MarkRequest incoming = gson.fromJson(json, MarkRequest.class);
            LOGGER.info(gson.toJson(incoming));

            int introvertCount = 0, extrovertCount = 0;
            String[] strings = incoming.getAnswers().replace(" ", "").split(";");
            for (int i = 0; i < incoming.getAnswerCount(); i++) {
                String text = strings[i];
                if (!text.equalsIgnoreCase("true") && !text.equalsIgnoreCase("false")) {
                    return badInputError().toJson();
                }
                if (Boolean.parseBoolean(text)) {
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

            return response(200, outcome).toJson();
        } catch (Exception e) {
            return exception(e);
        }
    }

    private static AppResponse badInputError() {
        String error = "Invalid answer value passed. Answers should only be true or false separated" +
                " by a semicolon(;)\nanswers:true;false;true;false;false;true";
        return response(500, error);
    }

    private static String exception(Exception e) {
        LOGGER.error(e.getMessage());
        return response(500, e.toString()).toJson();
    }

    private static AppResponse response(int status, Object data) {
        AppResponse response = new AppResponse(gson);
        response.setStatus(status);
        response.setData(data);
        return response;
    }
}

class AppResponse {
    @SerializedName("status")
    @Expose
    private int status;

    @SerializedName("data")
    @Expose
    private Object data;
    private transient final Gson gson;

    public AppResponse(Gson gson) {
        this.gson = gson;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String toJson() {
        return gson.toJson(this);
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