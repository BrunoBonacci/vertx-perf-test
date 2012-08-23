package com.brunobonacci.vertx.perf.bus.string;

import com.brunobonacci.vertx.perf.util.Constants;
import org.vertx.java.core.Handler;
import org.vertx.java.deploy.Verticle;

public class Producer extends Verticle {

    public void start() {
        System.out.println("Starting Producer");

        // sending messages to bus
        for (long i = 0L; i < Constants.TOTAL_RUN; i++)
            vertx.eventBus().send("destination", "simple string");

        System.out.println( Constants.TOTAL_RUN + " msg sent");
    }
}
