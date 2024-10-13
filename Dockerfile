FROM ubuntu:latest
LABEL authors="ocalderan"

FROM openjdk:17-jdk-alpine

WORKDIR /app

COPY build/libs/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]

EXPOSE 8080


