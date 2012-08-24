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

import groovy.json.JsonBuilder

settings = [
        outputDir: "./xperf-out",
        runner: "xperf-run.sh",
        configDir: "./xperf-config",
        cores: Runtime.runtime.availableProcessors(),

        tests: [
                // number
                [ pkg:"com.brunobonacci.vertx.perf.bus.number", prod:1, cons:1 ],
                [ pkg:"com.brunobonacci.vertx.perf.bus.number", prod:1, cons:2 ],
                [ pkg:"com.brunobonacci.vertx.perf.bus.number", prod:1, cons:3 ],
                [ pkg:"com.brunobonacci.vertx.perf.bus.number", prod:1, cons:4 ],
                [ pkg:"com.brunobonacci.vertx.perf.bus.number", prod:1, cons:5 ],
                [ pkg:"com.brunobonacci.vertx.perf.bus.number", prod:2, cons:1 ],
                [ pkg:"com.brunobonacci.vertx.perf.bus.number", prod:3, cons:1 ],
                [ pkg:"com.brunobonacci.vertx.perf.bus.number", prod:4, cons:1 ],
                [ pkg:"com.brunobonacci.vertx.perf.bus.number", prod:5, cons:1 ],
                [ pkg:"com.brunobonacci.vertx.perf.bus.number", prod:2, cons:2 ],
                [ pkg:"com.brunobonacci.vertx.perf.bus.number", prod:3, cons:3 ],
                //[ pkg:"com.brunobonacci.vertx.perf.bus.number", prod:4, cons:4 ],
                // String
                [ pkg:"com.brunobonacci.vertx.perf.bus.string", prod:1, cons:1 ],
                [ pkg:"com.brunobonacci.vertx.perf.bus.string", prod:1, cons:2 ],
                [ pkg:"com.brunobonacci.vertx.perf.bus.string", prod:1, cons:3 ],
                [ pkg:"com.brunobonacci.vertx.perf.bus.string", prod:1, cons:4 ],
                [ pkg:"com.brunobonacci.vertx.perf.bus.string", prod:1, cons:5 ],
                [ pkg:"com.brunobonacci.vertx.perf.bus.string", prod:2, cons:1 ],
                [ pkg:"com.brunobonacci.vertx.perf.bus.string", prod:3, cons:1 ],
                [ pkg:"com.brunobonacci.vertx.perf.bus.string", prod:4, cons:1 ],
                [ pkg:"com.brunobonacci.vertx.perf.bus.string", prod:5, cons:1 ],
                [ pkg:"com.brunobonacci.vertx.perf.bus.string", prod:2, cons:2 ],
                [ pkg:"com.brunobonacci.vertx.perf.bus.string", prod:3, cons:3 ],
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