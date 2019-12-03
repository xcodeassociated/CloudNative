FROM openjdk:11
ENV APP_HOME=/usr/local/app/
WORKDIR $APP_HOME
COPY ./build/libs/user-service-*.jar ./app.jar
EXPOSE 8761
EXPOSE 8888
EXPOSE 5432
EXPOSE 8100
CMD ["java","-jar","-Dspring.profiles.active=docker","app.jar"]
