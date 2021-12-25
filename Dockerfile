FROM openjdk:11
COPY target/*.jar lego-manager.jar
ENTRYPOINT ["java", "-jar", "/lego-manager.jar"]