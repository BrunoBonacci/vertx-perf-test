import groovy.json.JsonBuilder

settings = [
        outputDir: "./xperf-out",
        runner: "xperf-run.sh",
        configDir: "./xperf-config",
        cores: Runtime.runtime.availableProcessors(),

        tests: [
                [ pkg:"com.brunobonacci.vertx.perf.bus.number", prod:1, cons:1 ],
                [ pkg:"com.brunobonacci.vertx.perf.bus.number", prod:1, cons:2 ],
                [ pkg:"com.brunobonacci.vertx.perf.bus.number", prod:1, cons:3 ],
                [ pkg:"com.brunobonacci.vertx.perf.bus.number", prod:1, cons:4 ],
                [ pkg:"com.brunobonacci.vertx.perf.bus.number", prod:2, cons:1 ],
                [ pkg:"com.brunobonacci.vertx.perf.bus.number", prod:3, cons:1 ],
                [ pkg:"com.brunobonacci.vertx.perf.bus.number", prod:4, cons:1 ],
                [ pkg:"com.brunobonacci.vertx.perf.bus.number", prod:2, cons:2 ],
                [ pkg:"com.brunobonacci.vertx.perf.bus.number", prod:3, cons:3 ],
                [ pkg:"com.brunobonacci.vertx.perf.bus.number", prod:4, cons:4 ],
        ]
    ]
tools = [ ant: new AntBuilder() ]

suite = []

createConfig();
createRunner();



def createConfig(){

    tools.ant.mkdir( dir:settings.configDir );
    // for each test package generate the all set of config files
    settings.tests.each{
        test ->


        def cfg = new JsonBuilder();
        cfg(
            'test-package': test.pkg,
            "nProducers": test.prod,
            "nConsumers": test.cons,
            hasGenerator: false
        )

        test.qualifier = "${test.prod}x${test.cons}";
        def testCfg = new File( "$settings.configDir/config-${test.pkg}-${test.qualifier}.json")
        testCfg.text = cfg.toPrettyString();
        test.config = testCfg;
        suite << test;
    }
}



def createRunner(){

new File(settings.runner).text = """#!/bin/bash

export TS=\$( date +'%Y_%m_%d__%H_%M_%S' )

echo '(*) create output folder'
export OUTDIR=$settings.outputDir/\$TS
mkdir -p \$OUTDIR

echo '(*) copy configurations'
cp -R  $settings.configDir \$OUTDIR

echo '(*) RUNNING TESTS'
""" + settings.tests.collect{
"""

echo '  (-) running $it.pkg ${it.prod}x${it.cons}'
export VX_OUT_DIR=\$OUTDIR/$it.pkg/$it.qualifier
export VX_DATA_FILE=\$OUTDIR/vx-perf-data.csv
mkdir -p \$VX_OUT_DIR
./run.sh $it.config
"""
}.join("\n")

}