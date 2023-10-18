FROM maven:3-eclipse-temurin-21 AS builder
LABEL maintainer "David Perez Cabrera <dperezcabrera@gmail.com>"


COPY . /app/
COPY maven.xml /root/.m2/settings.xml

WORKDIR /app/
RUN mvn clean package
RUN mv target/*.jar target/demo-spring-boot.jar

FROM eclipse-temurin:21-jre

WORKDIR /app

COPY --from=builder /app/target/demo-spring-boot.jar /app/
ADD config/docker/docker-application.yaml config/application.yaml

EXPOSE 8080 8081

ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom ", "-jar", "demo-spring-boot.jar", "-Dspring.config.location=config/application.yaml"]
