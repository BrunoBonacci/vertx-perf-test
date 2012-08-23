package com.brunobonacci.vertx.perf.bus.stringReply;

import com.brunobonacci.vertx.perf.util.Constants;
import com.brunobonacci.vertx.perf.util.CounterVerticle;
import org.vertx.java.core.Handler;
import org.vertx.java.core.eventbus.Message;
import org.vertx.java.deploy.Verticle;

public class Generator extends Verticle {


    public void start() {
        System.out.println("Starting Generator");

        // sending messages to bus
        for (long i = 0L; i < Constants.TOTAL_RUN; i++)
            vertx.eventBus().send("producer", "simple string");
    }
}
