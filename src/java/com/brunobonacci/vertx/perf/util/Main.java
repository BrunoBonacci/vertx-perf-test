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

package com.brunobonacci.vertx.perf.util;

import org.vertx.java.core.Handler;
import org.vertx.java.core.json.JsonObject;
import org.vertx.java.deploy.Verticle;

public class Main extends Verticle {

    protected int nProducers = 1;
    protected int nConsumers = 1;
    protected String packg = null;

    public void start() {
        System.out.println("Initializing test...");

        final JsonObject cfg = container.getConfig();
        System.out.println("config: " + cfg);
        if (cfg == null) {
            throw new RuntimeException("Configuration not provided. please run: vertx run "+ getClass().getName() + " -cp classes -conf ./conf/a-config-file.json");
        }

        nProducers = cfg.getInteger(Constants.CFG_N_PRODUCERS) == 0 ? 1 : cfg.getInteger(Constants.CFG_N_PRODUCERS);
        nConsumers = cfg.getInteger(Constants.CFG_N_CONSUMERS) == 0 ? 1 : cfg.getInteger(Constants.CFG_N_CONSUMERS);
        packg = cfg.getString(Constants.CFG_TEST_PACKAGE);
        final boolean hasGenerator = cfg.getBoolean(Constants.CFG_HAS_GENERATOR, false);

        System.out.println("Starting bus perftest: " + packg);

        // deploying verticles
        container.deployVerticle( packg+".Consumer", cfg, nConsumers, new Handler<String>() {
            public void handle(String event) {
                container.deployVerticle(packg+".Producer", cfg, nProducers, new Handler<String>() {
                    public void handle(String event) {
                        if( hasGenerator )
                            container.deployVerticle(packg+".Generator", cfg);
                    }
                });
            }
        });
    }
}
