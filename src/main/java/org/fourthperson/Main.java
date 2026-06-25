package org.fourthperson;

import com.google.inject.Guice;
import com.google.inject.Injector;
import io.javalin.Javalin;

public class Main {
    public static void main(String[] args) {
        // Create the Guice injector with our AppModule
        final Injector injector = Guice.createInjector(new AppModule());

        // Get the Javalin instance from the Guice injector
        final Javalin app = injector.getInstance(Javalin.class);

        // Start Javalin server
        app.start();
    }
}