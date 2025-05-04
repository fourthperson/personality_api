package org.fourthperson;

public interface Config {
    String dbName = "pers_test";
    String dbUser = "root";
    String dbPass = "I@N2131";
    String databaseUrl = "jdbc:mariadb://localhost:3306/" + Config.dbName;
    int serverPort = 3030;
    String evaluationError = "Invalid answer data passed. Please refer to documentation for a sample.";
    String internalServerError = "An error occurred. Consult admin";
}
