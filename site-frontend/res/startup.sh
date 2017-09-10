#!/usr/bin/env bash
PID = ps -ef | grep site-frontend | awk '{print $2}'
kill -9 $PID
java -jar ${project.artifactId}
#TODO maven res 参数插件