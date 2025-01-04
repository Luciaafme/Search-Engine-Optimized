FROM openjdk:17-jdk-slim

# Crear un directorio de trabajo
WORKDIR /app

# Copiar el archivo JAR generado
COPY target/Indexer-1.0-SNAPSHOT.jar /app/app.jar

# Copiar la configuración de Hazelcast
COPY src/main/resources/indexer_server_hazelcast.xml /app/hazelcast.xml

# Exponer el puerto 5701
EXPOSE 5701

# Ejecutar el JAR
CMD ["java", "-jar", "/app/app.jar"]
