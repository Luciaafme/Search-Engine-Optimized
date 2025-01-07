FROM openjdk:17-jdk-slim

# Crear un directorio de trabajo
WORKDIR /app

# Copiar el archivo JAR del servidor Hazelcast
COPY target/Indexer-1.0-SNAPSHOT.jar /app/app.jar

# Copiar configuración del servidor Hazelcast
COPY src/main/resources/indexer_server_hazelcast.xml /app/resources/indexer_server_hazelcast.xml

# Exponer el puerto para Hazelcast
EXPOSE 5701

# Ejecutar el servidor Hazelcast
CMD ["java", "-jar", "/app/app.jar"]