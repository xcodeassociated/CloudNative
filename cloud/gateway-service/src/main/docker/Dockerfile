FROM openjdk:11
ENV APP_HOME=/usr/local/app/
WORKDIR $APP_HOME
COPY ./build/libs/gateway-service-*.jar ./app.jar
EXPOSE 8761
EXPOSE 8888
EXPOSE 8100
EXPOSE 8000
EXPOSE 8086
EXPOSE 9100
EXPOSE 9999
CMD ["java","-jar","-Dspring.profiles.active=docker","app.jar"]
