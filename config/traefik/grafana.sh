#!/bin/bash

cd `dirname $0`

docker run --rm -it \
    --network demo-network \
    --expose 3000 \
    --name grafana \
    --label 'app=demo' \
    --label 'component=grafana' \
    --label 'tier=monitoring' \
    --label 'traefik.enable=true' \
    --label 'traefik.http.routers.grafana.rule=Host(`grafana.demo-app.com`)' \
    --label 'traefik.http.routers.grafana.tls=true' \
    grafana/grafana
