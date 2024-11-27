FROM openjdk:17-jdk-slim AS builder

# 安装 Maven
RUN apt-get update && apt-get install -y maven

WORKDIR /app
COPY pom.xml .
COPY src ./src

# 构建项目
RUN mvn package -DskipTests

CMD ["java", "-jar", "/app/target/springboot-Backend-0.0.1-SNAPSHOT.jar", "--spring.profiles.active=prod"]
