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
