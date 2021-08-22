#! /bin/bash

docker run --rm -it \
    --volume "/var/run/docker.sock:/var/run/docker.sock:ro" \
    --volume "prometheus-discovery:/prometheus/discovery" \
    --name prometheus-discovery \
    --label 'traefik.enable=false' \
    --label 'app=demo' \
    --label 'component=prometheus-discover' \
    --label 'tier=monitoring' \
    dperezcabrera/prometheus-docker-discovery
