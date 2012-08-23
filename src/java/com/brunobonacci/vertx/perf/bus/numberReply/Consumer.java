package com.brunobonacci.vertx.perf.bus.numberReply;

import org.vertx.java.core.Handler;
import org.vertx.java.core.eventbus.Message;
import org.vertx.java.deploy.Verticle;

public class Consumer extends Verticle {

    public void start() {
        System.out.println("Starting Consumer");
        vertx.eventBus().registerHandler("destination", new Handler<Message<Long>>() {
            public void handle(Message<Long> message) {
                // access message body to avoid any lazy initialization logic
                long msg = message.body;
                message.reply(msg+1);
            }
        });
    }
}
