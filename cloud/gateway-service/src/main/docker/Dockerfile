FROM openjdk:11
ENV APP_HOME=/usr/local/app/
WORKDIR $APP_HOME
COPY ./build/libs/gateway-service-*.jar ./app.jar
EXPOSE 9999
CMD ["java","-jar","app.jar"]
