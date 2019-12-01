FROM openjdk:11
ENV APP_HOME=/usr/local/app/
WORKDIR $APP_HOME
COPY ./build/libs/zuul-service-*.jar ./app.jar
EXPOSE 8090
CMD ["java","-jar","-Dspring.profiles.active=docker","app.jar"]
