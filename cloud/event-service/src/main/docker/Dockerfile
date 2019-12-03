FROM openjdk:11
ENV APP_HOME=/usr/local/app/
WORKDIR $APP_HOME
COPY ./build/libs/event-service-*.jar ./app.jar
EXPOSE 8761
EXPOSE 8888
EXPOSE 27017
EXPOSE 8000
CMD ["java","-jar","-Dspring.profiles.active=docker","app.jar"]
