#!/bin/bash

cd `dirname $0`

. .env

INSTANCE="demo-spring-boot-$(date | sha1sum | cut -c1-8)"

docker run --rm -it \
    --network demo-network \
    --expose 8080 \
    --expose 8081 \
    --env APP_URL="https://web.demo-app.com/" \
    --env DB_URL=${DB_URL}  \
    --env DB_USERNAME=${DB_USERNAME} \
    --env DB_PASSWORD=${DB_PASSWORD} \
    --env JWT_SECRET=${JWT_SECRET} \
    --env DB_DRIVER=org.hibernate.dialect.PostgreSQLDialect \
    --env INSTANCE=${INSTANCE} \
    --volume "$PWD/application.yaml:/app/config/application.yaml" \
    --name "$INSTANCE" \
    --label 'app=demo' \
    --label 'component=demo-spring-boot' \
    --label 'tier=backend' \
    --label 'traefik.enable=true' \
    --label 'traefik.http.routers.demo-spring-boot.rule=Host(`web.demo-app.com`)' \
    --label 'traefik.http.routers.demo-spring-boot.tls=true' \
    --label 'traefik.http.services.demo-spring-boot.loadbalancer.server.port=8080' \
    --label 'prometheus-discovery.enabled=true' \
    --label 'prometheus-discovery.target='$INSTANCE':8081/actuator/prometheus' \
    --label 'prometheus-discovery.labels=tier=backend,app=demo' \
    'dperezcabrera/demo-spring-boot:0.0.1'