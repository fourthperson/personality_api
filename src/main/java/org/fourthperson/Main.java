package org.fourthperson;

import com.google.inject.Guice;
import com.google.inject.Injector;
import io.javalin.Javalin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        // Create the Guice injector with our AppModule
        final Injector injector = Guice.createInjector(new AppModule());

        // Get the Javalin instance from the Guice injector
        final Javalin app = injector.getInstance(Javalin.class);

        // Start Javalin server
        app.start();
    }
}