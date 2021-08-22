#!/bin/bash

. .env

docker run --rm -it \
    --network demo-network \
    --expose 5432 \
    --env POSTGRES_DB=${DB_NAME}  \
    --env POSTGRES_USER=${DB_USERNAME} \
    --env POSTGRES_PASSWORD=${DB_PASSWORD} \
    --volume demo-posrgres:/var/lib/postgresql/data \
    --label 'traefik.enable=false' \
    --name 'demo-postgres' \
    --label 'app=demo' \
    --label 'component=demo-postgres' \
    --label 'tier=database' \
    "postgres:12-alpine"
