FROM openjdk:17-jdk-slim

WORKDIR /app

COPY target/Crawler-1.0-SNAPSHOT.jar /app/app.jar

COPY src/main/resources/crawler_client_hazelcast.xml /app/crawler_client_hazelcast.xml

CMD ["java", "-Dhazelcast.client.config=/app/crawler_client_hazelcast.xml", "-jar", "/app/app.jar"]