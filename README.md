Vert.x Performance Suite
========================

This is a basic set of scripts to measure the performance of different aspects of Vert.x

### How to build and run a single test
Requires
 * JAVA 7
 * VERTX_HOME pointing to vert.x-1.2.3 or greater
 * Groovy 2 

Build with:

    ./clean.sh && ./mk.sh
    
Select test package to run and prepare config file like:

    $ vi config-bus-nuber-1x1.json
    -------8<-------8<-------8<-------8<-------8<-------8<-------8<-------
    {
      "test-package": "com.brunobonacci.vertx.perf.bus.number",
      "nProducers": 1,
      "nConsumers": 1,
      "hasGenerator": false
    }
    -------8<-------8<-------8<-------8<-------8<-------8<-------8<-------

Finally run single test with:

    ./run.sh config-bus-nuber-1x1.json

Aggregate results with:

    groovy scripts/PrepareStats.groovy ./vx-perf-data.csv > aggredated-stats.csv

Check results into:
  * **cpu-profile.txt** - with CPU metrics
  * **gc-profile.txt** - JVM GC activity, you can graph this using [GCViewer](http://www.tagtraum.com/gcviewer.html)
  * Import **aggredated-stats.csv** into Excel or OpenOffice and graph results.


### Documentation and Results
  * [Documentation](https://github.com/BrunoBonacci/vertx-perf-test/wiki)


