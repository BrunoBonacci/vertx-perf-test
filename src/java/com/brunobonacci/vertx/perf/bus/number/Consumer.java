package com.brunobonacci.vertx.perf.bus.number;

import com.brunobonacci.vertx.perf.util.CounterVerticle;
import org.vertx.java.core.Handler;
import org.vertx.java.core.eventbus.Message;

public class Consumer extends CounterVerticle {

    public void start() {
        System.out.println("Starting Consumer");
        super.start();
        vertx.eventBus().registerHandler("destination", new Handler<Message<Long>>() {
            public void handle(Message<Long> message) {
                // access message body to avoid any lazy initialization logic
                long msg = message.body;
                count();
            }
        });
    }
}
