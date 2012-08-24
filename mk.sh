#!/bin/bash

rm -fr output
mkdir output
javac -cp $( echo `find $VERTX_HOME/lib/ -name \*.jar` | sed 's/ /:/g' ) $(find src/java -name \*.java) -d output
