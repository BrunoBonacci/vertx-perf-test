#!/bin/bash

rm -fr output/* && javac -cp $( echo `find /opt/vertx/lib/ -name \*.jar` | sed 's/ /:/g' ) $(find src/java -name \*.java) -d output
