#!/bin/bash

export VX_OUT_DIR=${VX_OUT_DIR:-.}

export JAVA_OPTS=""
export JAVA_OPTS=-agentlib:hprof=cpu=samples,interval=100,depth=3,format=a,file=${VX_PROFILE_CPU:-$VX_OUT_DIR/cpu-profile.txt}
export JAVA_OPTS="$JAVA_OPTS -Dcom.sun.management.jmxremote -Dcom.sun.management.jmxremote.port=8888 -Dcom.sun.management.jmxremote.authenticate=false -Dcom.sun.management.jmxremote.ssl=false"
export JAVA_OPTS="$JAVA_OPTS -verbose:gc  -Xloggc:${VX_PROFILE_GC:-$VX_OUT_DIR/gc-profile.txt} -XX:+PrintGCDetails"

vertx run com.brunobonacci.vertx.perf.util.Main -cp output/:./config -conf $1
