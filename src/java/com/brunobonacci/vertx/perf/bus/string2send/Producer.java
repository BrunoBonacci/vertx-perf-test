package com.brunobonacci.vertx.perf.bus.string2send;

import com.brunobonacci.vertx.perf.util.Constants;
import com.brunobonacci.vertx.perf.util.CounterVerticle;
import org.vertx.java.core.Handler;
import org.vertx.java.core.eventbus.Message;
import org.vertx.java.deploy.Verticle;

public class Producer extends Verticle {


    public void start() {
        System.out.println("Starting Producer");

        // sending messages to bus
        vertx.eventBus().registerHandler("producer", new Handler<Message<String>>() {
            public void handle(Message<String> message) {
                vertx.eventBus().send("destination", "simple string");
            }
        });
    }
}
