FROM openjdk:17-slim
LABEL maintainer "David Perez Cabrera <dperezcabrera@gmail.com>"

WORKDIR /app

COPY target/*.jar demo-spring-boot.jar
ADD config/docker/docker-application.yaml config/application.yaml

EXPOSE 8080 8081

ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom ", "-jar", "demo-spring-boot.jar", "-Dspring.config.location=config/application.yaml"]
