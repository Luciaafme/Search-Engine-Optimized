FROM openjdk:17-jdk-slim

WORKDIR /app

COPY target/Indexer-1.0-SNAPSHOT.jar /app/app.jar

EXPOSE 8080

CMD ["java", "-jar", "/app/app.jar"]