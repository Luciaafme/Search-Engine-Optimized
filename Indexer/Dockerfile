FROM openjdk:11-jre-slim

WORKDIR /app
COPY target/*.jar app.jar
COPY src/main/resources/indexer_server_hazelcast.xml /app/hazelcast.xml

# Add SLF4J dependency
RUN mkdir -p /app/lib
ADD https://repo1.maven.org/maven2/org/slf4j/slf4j-api/1.7.36/slf4j-api-1.7.36.jar /app/lib/
ADD https://repo1.maven.org/maven2/org/slf4j/slf4j-simple/1.7.36/slf4j-simple-1.7.36.jar /app/lib/

ENV JAVA_OPTS="--add-modules java.se --add-exports java.base/jdk.internal.ref=ALL-UNNAMED --add-opens java.base/java.lang=ALL-UNNAMED --add-opens java.base/java.nio=ALL-UNNAMED --add-opens java.base/sun.nio.ch=ALL-UNNAMED --add-opens java.management/sun.management=ALL-UNNAMED --add-opens jdk.management/com.sun.management.internal=ALL-UNNAMED"

EXPOSE 5701
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -cp 'app.jar:lib/*' control.IndexerMain"]