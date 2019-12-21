#!/bin/bash

rm -rf eureka-service/build
cd eureka-service/
export JAVA_HOME=`/usr/libexec/java_home -v 1.8`;
gradle clean build && docker build -t eureka-service .
cd ..

rm -rf zuul-service/build
cd zuul-service
export JAVA_HOME=`/usr/libexec/java_home -v 1.8`;
gradle clean build && docker build -t zuul-service .
cd ..

rm -rf configuration-service/build
cd configuration-service/
export JAVA_HOME=`/usr/libexec/java_home -v 11`;
gradle clean build && docker build -t configuration-service .
cd ..

rm -rf gateway-service/build
cd gateway-service
export JAVA_HOME=`/usr/libexec/java_home -v 11`;
gradle clean build && docker build -t gateway-service .
cd ..

rm -rf user-service/build
cd user-service
export JAVA_HOME=`/usr/libexec/java_home -v 11`;
gradle clean build && docker build -t user-service .
cd ..

rm -rf event-service/build
cd event-service
export JAVA_HOME=`/usr/libexec/java_home -v 11`;
gradle clean build && docker build -t event-service .
cd ..
