FROM openjdk:17-jdk-slim

# Crear un directorio de trabajo
WORKDIR /app

# Copiar el archivo JAR generado
COPY target/QueryEngine-1.0-SNAPSHOT.jar /app/app.jar

# Copiar la configuración de Hazelcast
COPY src/main/resources/qurey_client_hazelcast.xml /app/hazelcast-client.xml

EXPOSE 8080

# Ejecutar el cliente Hazelcast
CMD ["java","-Dhazelcast.client.config=/app/hazelcast-client.xml", "-jar", "/app/app.jar"]
