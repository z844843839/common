# Start with a base image containing Java runtime
FROM registry.cn-shenzhen.aliyuncs.com/base_repo/java_8u212_cst:v1.0.0

# Add Maintainer Info
MAINTAINER honux <master@e6yun.com>

# 设置locale
ENV LANG en_US.UTF-8
ENV LANGUAGE en_US:en
ENV LC_ALL en_US.UTF-8
ENV TZ=Asia/Shanghai

RUN mkdir /app \
    mkdir /opt/settings && echo "env=<ENV>" > /opt/settings/server.properties

WORKDIR /app

COPY eureka-server/target/eureka-server-1.0.0-SNAPSHOT.jar /app/demo.jar
# COPY server.properties /opt/settings/server.properties


EXPOSE 8080

ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-Dserver.port=8080", "-jar","/app/demo.jar"]
