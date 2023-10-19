FROM openjdk:20-jdk-slim
ARG JAR_FILE=target/*.jar
EXPOSE 8080
COPY ./target/apiTesting-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]