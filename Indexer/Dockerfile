FROM openjdk:17-jdk-slim

WORKDIR /app

COPY target/Indexer-1.0-SNAPSHOT.jar /app/app.jar

COPY src/main/resources/indexer_client_hazelcast.xml /app/resources/indexer_client_hazelcast.xml

EXPOSE 8080

CMD ["java", "-jar", "/app/app.jar"]