/*
   Copyright 2012 - Bruno Bonacci

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/

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
