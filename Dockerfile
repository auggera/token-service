FROM openjdk:21-jdk

WORKDIR /app

COPY target/token-service-0.0.1-SNAPSHOT.jar /app/token-service.jar

EXPOSE 8082

ENTRYPOINT ["java", "-jar", "token-service.jar"]
