FROM openjdk:8-jdk-alpine
COPY "springboot-Backend-0.0.1.jar" /app.jar
EXPOSE 9666
ENTRYPOINT ["java", "-jar", "/app.jar"]