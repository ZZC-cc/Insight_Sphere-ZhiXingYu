# 使用轻量级 JDK 运行时镜像
FROM openjdk:17-jdk-slim

# 定义工作目录
WORKDIR /app

# 将本地打包好的 JAR 文件复制到容器中
COPY target/springboot-Backend-0.0.1.jar /app/app.jar

EXPOSE 9666

# 运行 Spring Boot 应用
CMD ["java", "-jar", "/app/app.jar", "--spring.profiles.active=prod"]
