# Usa una imagen ligera de Java para la ejecución
FROM openjdk:17-jdk-slim

# Establece el directorio de trabajo
WORKDIR /app

# Copia el archivo jar compilado localmente al contenedor
COPY target/Crawler-1.0-SNAPSHOT.jar app.jar

# Copia el archivo hazelcast-client.xml al contenedor
COPY src/main/resources/crawler_client_hazelcast.xml /app/config/crawler_client_hazelcast.xml

# Puerto expuesto (ajustar si necesario)
EXPOSE 8080

# Comando para ejecutar la aplicación y especificar la ubicación del archivo de configuración
ENTRYPOINT ["java", "-jar", "app.jar", "--hazelcast.config=/app/config/crawler_client_hazelcast.xml"]
