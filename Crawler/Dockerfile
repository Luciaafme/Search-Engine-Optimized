FROM openjdk:17-jdk-slim


# Crear un directorio de trabajo
WORKDIR /app

# Copiar el archivo JAR generado
COPY target/Crawler-1.0-SNAPSHOT.jar /app/app.jar

# Copiar la configuración de Hazelcast
COPY src/main/resources/crawler_client_hazelcast.xml /app/hazelcast-client.xml

# Ejecutar el cliente Hazelcast
CMD ["java", "-jar", "/app/app.jar"]