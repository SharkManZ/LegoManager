FROM openjdk:11
COPY target/*.jar lego-manager.jar
ENTRYPOINT ["java", "-Dspring.profiles.active=PROM", "-jar", "/lego-manager.jar"]