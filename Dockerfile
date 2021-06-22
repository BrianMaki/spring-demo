FROM openjdk:11-jdk-slim

ARG SERVICE_NAME=demo

COPY ./target/demo-0.0.1-SNAPSHOT.jar /app/demo.jar

CMD ["java", "-jar", "/app/demo.jar"]

