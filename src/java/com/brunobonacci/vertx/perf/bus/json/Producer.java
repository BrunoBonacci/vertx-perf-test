package com.brunobonacci.vertx.perf.bus.json;

import com.brunobonacci.vertx.perf.util.Constants;
import org.vertx.java.core.json.JsonObject;
import org.vertx.java.deploy.Verticle;

public class Producer extends Verticle {

    public void start() {
        System.out.println("Starting Producer");

        // building the message
        JsonObject details = new JsonObject();
        details.putString("firstname", "John");
        details.putString("lastname", "Smith");
        details.putNumber("age", 42);
        details.putString("telephone", "+44.111.222.333.444");
        details.putString("email", "john.smith@whatever.com");

        JsonObject address = new JsonObject();
        address.putString("city", "London");
        address.putString("number", "1/A");
        address.putString("street", "Regent's Street");
        address.putString("county", "Greater London");
        address.putString("country", "UK");

        details.putObject("address", address);

        // sending messages to bus
        for (long i = 0L; i < Constants.TOTAL_RUN; i++) {
            // put a dynamic value to avoid any seralization caching
            details.putNumber("ever-changing", i);
            vertx.eventBus().send("destination", details);
        }

        System.out.println( Constants.TOTAL_RUN + " msg sent");
    }
}
