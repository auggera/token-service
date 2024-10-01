FROM openjdk:21-jdk

WORKDIR /app

COPY target/token-service.jar /app/token-service.jar

EXPOSE 8082

ENTRYPOINT ["java", "-jar", "/app/token-service.jar"]
