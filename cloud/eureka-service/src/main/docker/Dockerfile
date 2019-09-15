FROM openjdk:8
ENV APP_HOME=/usr/local/app/
WORKDIR $APP_HOME
COPY ./build/libs/eureka-service-*.jar ./app.jar
EXPOSE 8761
CMD ["java","-jar","app.jar"]
