package com.brunobonacci.vertx.perf.bus.stringReply;

import com.brunobonacci.vertx.perf.util.Constants;
import com.brunobonacci.vertx.perf.util.CounterVerticle;
import org.vertx.java.core.Handler;
import org.vertx.java.core.eventbus.Message;
import org.vertx.java.deploy.Verticle;

public class Producer extends CounterVerticle {


    public void start() {
        System.out.println("Starting Producer");
        super.start();

        final Handler<Message<String>> handleReply = new Handler<Message<String>>() {
            public void handle(Message<String> message) {
                // access message body to avoid any lazy initialization logic
                String msg = message.body;
                long counter = count();
                if( counter == Constants.TOTAL_RUN)
                    System.out.println( Constants.TOTAL_RUN + " msg sent");
            }
        };

        // sending messages to bus
        vertx.eventBus().registerHandler("producer", new Handler<Message<String>>() {
            public void handle(Message<String> message) {
                vertx.eventBus().send("destination", "simple string", handleReply);
            }
        });
    }
}
