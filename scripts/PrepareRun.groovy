import groovy.json.JsonBuilder

settings = [
        outputDir: "./xperf-out",
        runner: "xperf-run.sh",
        configDir: "./xperf-config",
        cores: Runtime.runtime.availableProcessors(),

        tests: [
                "com.brunobonacci.vertx.perf.bus.number",
                "com.brunobonacci.vertx.perf.bus.string",
        ]
    ]
tools = [ ant: new AntBuilder() ]

createConfig();
createRunner();



def createConfig(){

    tools.ant.mkdir( dir:settings.configDir );
    // for each test package generate the all set of config files
    settings.tests.each{
        test ->


        def cfg = new JsonBuilder();
        cfg(
            'test-package': test,
            "nProducers": 1,
            "nConsumers": 1,
            hasGenerator: false
        )

        new File( "$settings.configDir/config-${test}.json").text = cfg.toPrettyString();
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

echo '  (-) running $it'
export VX_OUT_DIR=\$OUTDIR/$it
mkdir -p \$VX_OUT_DIR
./run.sh $settings.configDir/config-${it}.json
"""
}.join("\n")

}