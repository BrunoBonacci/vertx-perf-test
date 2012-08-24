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
