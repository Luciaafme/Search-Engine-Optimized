FROM openjdk:17

WORKDIR /app

COPY target/QueryEngine-1.0-SNAPSHOT.jar /app/app.jar

COPY src/main/resources/query_server_hazelcast.xml /app/query_server_hazelcast.xml

EXPOSE 5701

CMD ["java", "-Dhazelcast.config=/app/query_client_hazelcast.xml", "-jar", "/app/app.jar"]