package com.brunobonacci.vertx.perf.util;

public interface  Constants {

    long TOTAL_RUN = 100_000_000L;
    int  SAMPLE_EVERY = 5000; // millis

    String CFG_TEST_PACKAGE = "test-package";
    String CFG_N_PRODUCERS = "nProducers";
    String CFG_N_CONSUMERS = "nConsumers";
    String CFG_HAS_GENERATOR = "hasGenerator";

}
