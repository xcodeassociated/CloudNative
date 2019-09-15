FROM openjdk:11
ENV APP_HOME=/usr/local/app/
WORKDIR $APP_HOME
COPY ./build/libs/configuration-service-*.jar ./app.jar
EXPOSE 8888
CMD ["java","-jar","app.jar"]
