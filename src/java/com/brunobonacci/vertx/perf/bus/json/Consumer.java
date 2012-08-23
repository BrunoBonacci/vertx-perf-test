package com.brunobonacci.vertx.perf.bus.json;

import com.brunobonacci.vertx.perf.util.CounterVerticle;
import org.vertx.java.core.Handler;
import org.vertx.java.core.eventbus.Message;
import org.vertx.java.core.json.JsonObject;

public class Consumer extends CounterVerticle {

    public void start() {
        System.out.println("Starting Consumer");
        vertx.eventBus().registerHandler("destination", new Handler<Message<JsonObject>>() {
            public void handle(Message<JsonObject> message) {
                // access message body to avoid any lazy initialization logic
                String msg = message.body.getObject("address").getString("street");
                count();
            }
        });
    }
}
