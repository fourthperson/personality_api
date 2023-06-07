package org.fourthperson;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.jdbc.JdbcPooledConnectionSource;
import com.j256.ormlite.table.DatabaseTable;
import com.squareup.moshi.Json;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;

import static spark.Spark.*;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    private static JsonAdapter<MarkRequest> markRequestJsonAdapter;
    private static JsonAdapter<AppResponse> appResponseJsonAdapter;

    public static void main(String[] args) {
        Moshi moshi = new Moshi.Builder().build();
        markRequestJsonAdapter = moshi.adapter(MarkRequest.class);
        appResponseJsonAdapter = moshi.adapter(AppResponse.class);

        Dao<Question, String> questionDao;

        try {
            JdbcPooledConnectionSource connectionSource = new JdbcPooledConnectionSource(Config.databaseUrl);
            connectionSource.setUsername(Config.dbUser);
            connectionSource.setPassword(Config.dbPass);

            questionDao = DaoManager.createDao(connectionSource, Question.class);
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }

        port(3030);

        get("/", (request, response) -> {
            halt(403);
            return request.body();
        });

        get("/questions", (request, response) -> {
            response.type(Config.contentType);
            response.body(getQuestions(questionDao));
            return response.body();
        });

        post("/evaluate", (request, response) -> {
            response.type(Config.contentType);
            response.body(mark(request.body()));
            return response.body();
        });

        redirect.get("/questions/", "/questions");

        redirect.post("/evaluate/", "/evaluate");
    }

    private static String getQuestions(Dao<Question, String> dao) {
        try {
            List<Question> questions = dao.queryForAll();
            Collections.shuffle(questions);
            return response(200, questions).toJson();
        } catch (Exception e) {
            return exception(e);
        }
    }

    private static String mark(String json) {
        try {
            MarkRequest incoming = markRequestJsonAdapter.fromJson(json);
            if (incoming == null) {
                return badInputError().toJson();
            }

            logger.info(markRequestJsonAdapter.toJson(incoming));

            int introCount = 0, extroCount = 0;
            String[] strings = incoming.getAnswers().replace(" ", "").split(";");
            for (int i = 0; i < incoming.getAnswerCount(); i++) {
                String text = strings[i];
                if (!text.equalsIgnoreCase("true") && !text.equalsIgnoreCase("false")) {
                    return badInputError().toJson();
                }
                if (Boolean.parseBoolean(text)) {
                    introCount++;
                } else {
                    extroCount++;
                }
            }

            String res = introCount > extroCount ? "Introverted" : extroCount > introCount ? "Extroverted" : "Balanced";

            return response(200, res).toJson();
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
        logger.error(e.getMessage());
        return response(500, e.toString()).toJson();
    }

    private static AppResponse response(int status, Object data) {
        AppResponse response = new AppResponse(appResponseJsonAdapter);
        response.setStatus(status);
        response.setData(data);
        return response;
    }
}

class AppResponse {
    @Json(name = "status")
    private int status;
    @Json(name = "data")
    private Object data;

    private transient final JsonAdapter<AppResponse> jsonAdapter;

    public AppResponse(JsonAdapter<AppResponse> adapter) {
        this.jsonAdapter = adapter;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String toJson() {
        return jsonAdapter.toJson(this);
    }
}


class MarkRequest {
    @Json(name = "answer_count")
    private Integer answerCount;
    @Json(name = "answers")
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

interface Config {
    String dbName = "pers_test";
    String dbUser = "root";
    String dbPass = "I@N2131";
    String contentType = "application/json";
    String databaseUrl = "jdbc:mariadb://localhost:3306/" + Config.dbName;
}