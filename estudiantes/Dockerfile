FROM openjdk:17-jdk-slim
VOLUME /tmp
COPY target/estudiantes-0.0.1-SNAPSHOT.jar estudiantes.jar
ENTRYPOINT ["java", "-jar", "/estudiantes.jar"]
