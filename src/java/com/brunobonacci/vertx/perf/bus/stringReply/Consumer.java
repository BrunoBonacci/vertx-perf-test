package com.brunobonacci.vertx.perf.bus.stringReply;

import com.brunobonacci.vertx.perf.util.CounterVerticle;
import org.vertx.java.core.Handler;
import org.vertx.java.core.eventbus.Message;
import org.vertx.java.deploy.Verticle;

public class Consumer extends Verticle {

    public void start() {
        System.out.println("Starting Consumer");
        vertx.eventBus().registerHandler("destination", new Handler<Message<String>>() {
            public void handle(Message<String> message) {
                // access message body to avoid any lazy initialization logic
                String msg = message.body;
                message.reply("Simple response");
            }
        });
    }
}
