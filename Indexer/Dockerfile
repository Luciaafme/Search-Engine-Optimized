# Usar una imagen ligera de Java para la ejecución
FROM openjdk:17-jdk-slim

# Establecer el directorio de trabajo
WORKDIR /app

# Copiar el archivo jar compilado localmente al contenedor
COPY target/Indexer-1.0-SNAPSHOT.jar /app/indexer.jar

# Copiar el archivo de configuración de Hazelcast
COPY src/main/resources/indexer_server_hazelcast.xml /app/config/indexer_server_hazelcast.xml

# Exponer el puerto de Hazelcast
EXPOSE 5701

# Comando para ejecutar el Indexer
ENTRYPOINT ["java", "-jar", "indexer.jar", "--hazelcast.config=/app/config/indexer_server_hazelcast.xml"]
