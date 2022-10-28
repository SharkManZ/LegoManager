#
# Build stage
#
FROM maven:3.8.4-jdk-11-slim AS build
COPY src /home/lego-manager/src
COPY pom.xml /home/lego-manager
RUN mvn -f /home/lego-manager/pom.xml clean package -P env.dev.prod
#
# Package stage
#
FROM openjdk:11
COPY target/*.jar lego-manager.jar
ENTRYPOINT ["java", "-Dspring.profiles.active=PROM", "-jar", "/lego-manager.jar"]