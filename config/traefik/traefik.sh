#!/bin/bash

cd `dirname $0`

docker run --rm -it \
    --network demo-network \
    --publish 80:80 \
    --publish 443:443 \
    --volume "/var/run/docker.sock:/var/run/docker.sock:ro" \
    --volume "$PWD/config:/config" \
    --name traefik  \
    --label 'component=traefik' \
    --label 'tier=load-balancer' \
    --label 'traefik.http.routers.dashboard.rule=Host(`traefik.demo-app.com`)' \
    --label 'traefik.http.routers.dashboard.service=api@internal' \
    --label 'traefik.http.routers.dashboard.middlewares=auth' \
    --label 'traefik.http.routers.dashboard.tls=true' \
    --label 'traefik.http.middlewares.auth.basicauth.users=admin:$apr1$6hHvW1K5$VGJRGqMrbjv1c3RhrdqQh.' \
    --label 'traefik.http.routers.http-catchall.rule=hostregexp(`{host:[a-z-.]+}`)' \
    --label 'traefik.http.routers.http-catchall.entrypoints=web' \
    --label 'traefik.http.routers.http-catchall.middlewares=redirect-to-https' \
    --label 'traefik.http.middlewares.redirect-to-https.redirectscheme.scheme=https' \
    traefik \
        --api.dashboard=true \
        --providers.docker \
        --entrypoints.web.address=:80 \
        --entrypoints.web-secure.address=:443 \
        --providers.file.directory=/config/ \
        --providers.file.watch=true

# htpasswd  -nb  admin "$PASSWORD"
