#!/bin/sh
# RSE Launcher

DEBUG=
#DEBUG=-agentlib:jdwp=transport=dt_socket,server=y,address=8888



if [ "x$CLASSPATH" = "x" ]; then
	CLASSPATH=".";
else
	CLASSPATH=$CLASSPATH:.
fi;

for jar in `ls $RSE_HOME/lib/*.jar`; do
	CLASSPATH=$CLASSPATH:$jar
done;



java -Xmx512m $DEBUG -cp $CLASSPATH org.gwe.rse.servlet.jetty.WebServerApp $RSE_HOME $@

