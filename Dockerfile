# Docker 镜像构建

# 使用阿里云镜像加速器拉取基础镜像
FROM dvudijk2.mirror.aliyuncs.com/library/maven:3.8.7-openjdk-17 AS build


# 解决容器时期与真实时间相差 8 小时的问题
RUN ln -snf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime && echo Asia/Shanghai > /etc/timezone

# 复制代码到容器内
WORKDIR /app
COPY pom.xml .
COPY src ./src

# 打包构建
RUN mvn package -DskipTests

# 容器启动时运行 jar 包
CMD ["java","-jar","/springboot-Backend/target/springboot-Backend-0.0.1.jar","--spring.profiles.active=prod"]