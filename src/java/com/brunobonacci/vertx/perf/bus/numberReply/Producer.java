package com.brunobonacci.vertx.perf.bus.numberReply;

import com.brunobonacci.vertx.perf.util.Constants;
import com.brunobonacci.vertx.perf.util.CounterVerticle;
import org.vertx.java.core.Handler;
import org.vertx.java.core.eventbus.Message;

public class Producer extends CounterVerticle {


    public void start() {
        System.out.println("Starting Producer");
        super.start();

        Handler<Message<Long>> handleReply = new Handler<Message<Long>>() {
            public void handle(Message<Long> message) {
                // access message body to avoid any lazy initialization logic
                long msg = message.body;
                long counter = count();
                if( counter == Constants.TOTAL_RUN)
                    System.out.println( Constants.TOTAL_RUN + " msg sent");
            }
        };

        // sending messages to bus
        for (long i = 0L; i < Constants.TOTAL_RUN; i++)
            vertx.eventBus().send("destination", i, handleReply);
    }
}
