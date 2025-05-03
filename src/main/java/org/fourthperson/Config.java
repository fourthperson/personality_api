package org.fourthperson;

public interface Config {
    String dbName = "pers_test";
    String
            dbUser = "root";
    String dbPass = "I@N2131";
    String contentType = "application/json";
    String databaseUrl = "jdbc:mariadb://localhost:3306/" + Config.dbName;

    int serverPort = 3030;
    String evaluationError = "Invalid answer value passed. Answers should only be true or false separated" +
            " by a semicolon(;)\nanswers:true;false;true;false;false;true";
}
