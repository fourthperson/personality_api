package org.fourthperson;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.Properties;

public class Config {
    private static final Logger logger = LoggerFactory.getLogger(Config.class);
    private static final Properties properties = new Properties();

    static {
        try (InputStream input = Config.class.getClassLoader().getResourceAsStream("application.properties")) {
            if (input == null) {
                logger.warn("Sorry, unable to find application.properties");
            }
            properties.load(input);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
    }

    public static final String databaseUrl = properties.getProperty("db.url", "");
    public static final String dbUser = properties.getProperty("db.user", "");
    public static final String dbPass = properties.getProperty("db.pass", "");
    public static final int serverPort = Integer.parseInt(properties.getProperty("server.port", "-1"));
    public static final String internalServerError = properties.getProperty("error.internal", "Internal server error");
    public static final String evaluationError = properties.getProperty("error.evaluation", "Evaluation error");
}
    