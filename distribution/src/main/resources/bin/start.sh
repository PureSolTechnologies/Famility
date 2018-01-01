#!/bin/sh

# Evaluating FAMILITY_HOME
FAMILITY_HOME="$( cd "$( dirname $0 )/.." && pwd )"

# JVM arguments...
JVM_ARGS="-Dfamility.home=$FAMILITY_HOME"
# Debug arguments 
#JVM_ARGS="$JVM_ARGS -agentlib:jdwp=transport=dt_socket,server=y,address=8000,suspend=y" 

# Running Famility...
java $JVM_ARGS -jar $FAMILITY_HOME/lib/framework-${project.version}.jar $FAMILITY_HOME/etc/framework.yml
