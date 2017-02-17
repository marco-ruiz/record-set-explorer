#!/bin/sh
# Sets the classpath to include all jars under the lib folder

if [ "x$CLASSPATH" = "x" ]; then
	CLASSPATH=".";
else
	CLASSPATH=$CLASSPATH:.
fi;

for jar in `ls $RSE_HOME/lib/*.jar`; do
	CLASSPATH=$CLASSPATH:$jar
done;

