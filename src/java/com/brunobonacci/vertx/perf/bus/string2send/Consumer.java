package com.brunobonacci.vertx.perf.bus.string2send;

import com.brunobonacci.vertx.perf.util.CounterVerticle;
import org.vertx.java.core.Handler;
import org.vertx.java.core.eventbus.Message;

public class Consumer extends CounterVerticle {

    public void start() {
        System.out.println("Starting Consumer");
        super.start();
        vertx.eventBus().registerHandler("destination", new Handler<Message<String>>() {
            public void handle(Message<String> message) {
                // access message body to avoid any lazy initialization logic
                String msg = message.body;
                count();
            }
        });
    }
}
