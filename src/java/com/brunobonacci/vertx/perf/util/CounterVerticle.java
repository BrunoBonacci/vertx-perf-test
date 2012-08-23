package com.brunobonacci.vertx.perf.util;

import org.vertx.java.core.Handler;
import org.vertx.java.core.eventbus.Message;
import org.vertx.java.core.json.JsonObject;
import org.vertx.java.deploy.Verticle;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.Writer;
import java.lang.management.OperatingSystemMXBean;
import java.util.ArrayList;
import java.util.List;

public abstract class CounterVerticle extends Verticle {

    private boolean displayStats = Boolean.parseBoolean(System.getProperty("showStats", "true"));

    private List<StatRecord> stats = new ArrayList<>(100);

    private long counter = 0L;
    private long lastCount = 0L;
    private long lastTime = 0L;

    private long timerCount = 0L;

    private int  nCPU = Runtime.getRuntime().availableProcessors();
    private OperatingSystemMXBean OSmx = java.lang.management.ManagementFactory.getOperatingSystemMXBean();

    public void start() {

        long timerID = vertx.setPeriodic( Constants.SAMPLE_EVERY, new Handler<Long>() {
            public void handle(Long timerID) {
                // this check every five seconds, if no new msg are processed,
                // then it assume that it is terminated and exit.
                if( timerCount != 0 && timerCount == counter ){
                    System.out.println("Nothing in the past 5 seconds, then exit!");
                    dumpStats();
                    // give time to others to write stats. FIXME
                    try{ Thread.currentThread().sleep(2000);} catch( Exception x) {}
                    container.exit();
                } else {
                    // otherwise update counter
                    timerCount = counter;

                    // record time elapsed since last sampling
                    long curTime = System.nanoTime();
                    long time = ((curTime - lastTime) / 1000000);

                    // record processed count since last sampling
                    long processed = counter - lastCount;

                    // record avg cpu load
                    int load = (int)(OSmx.getSystemLoadAverage() * 100 / nCPU);

                    if( displayStats )
                        System.out.println(processed+" msg received in: " + time + " ms --> " + ((long) processed / time) + "K msg/s  @ " + load + "% of CPU");

                    // record statistics
                    stats.add( new StatRecord(time, processed, load ));

                    lastCount = counter;
                    lastTime  = curTime;
                }
            }
        });
    }

    protected long count() {
        ++counter;
        if (lastTime == 0L)
            lastTime = System.nanoTime();

        return counter;
    }



    private void dumpStats(){

        String filename = System.getProperty("vx.perf.data", "vx-perf-data.csv");
        System.out.println( "Appending statistics to: " + filename );

        // remove first 5 sec for JVM warm up
        for(int i = 0; i < (int)(5000 / Constants.SAMPLE_EVERY); i++ )
            stats.remove(0);

        // remove last because in most case is going to be wrong
        // because sampling will happens long after is finished
        stats.remove(stats.size()-1);

        if( stats.size() < 3 ){
            System.out.println("ERROR: Too few samples...");
            return;
        }

        // calculate avg msg/sec
        // and avg cpu load
        long avgThroughput = 0;
        long avgCpuLoad = 0;

        for( StatRecord rec : stats ){
            avgThroughput += rec.processed * 1000 / rec.timeMillis;
            // Java return the average cpu load for the past minute
            // so last elements tend to be more precise than the beginning
            // therefore i will use only the last element.
            avgCpuLoad = rec.avgCpuLoad;
        }

        avgThroughput /= stats.size();

        JsonObject cfg = container.getConfig();

        int nProducers = cfg.getInteger(Constants.CFG_N_PRODUCERS) == 0 ? 1 : cfg.getInteger(Constants.CFG_N_PRODUCERS);
        int nConsumers = cfg.getInteger(Constants.CFG_N_CONSUMERS) == 0 ? 1 : cfg.getInteger(Constants.CFG_N_CONSUMERS);
        String packg = cfg.getString(Constants.CFG_TEST_PACKAGE);

        String output = String.format("%s, %d, %d, %d, %d\n", packg, nProducers, nConsumers, avgThroughput, avgCpuLoad );

        try ( Writer out = new BufferedWriter(new FileWriter(filename, true))) {
            out.write( output );
            out.flush();
        } catch (Exception x){
            throw new RuntimeException("Unable to write into file:" + filename, x);
        }
    }


    private static class StatRecord {
        long timeMillis = 0L;
        long processed = 0L;
        int  avgCpuLoad = 0;

        StatRecord( long timeMillis, long processed, int avgCpuLoad){
            this.timeMillis = timeMillis;
            this.processed = processed;
            this.avgCpuLoad = avgCpuLoad;
        }
    }


}
