package org.example;

public interface Config {
    String DB_NAME = "pers_test";
    String DB_USER = "server_user";
    String DB_PASS = "Pass1@34%";
    String CONTENT_TYPE = "application/json";
    String databaseUrl = "jdbc:mariadb://localhost:3306/" + Config.DB_NAME;
}
