#! /bin/bash

cd `dirname $0`

function create_main_cert() {
    DOMAIN=$1
    openssl req -new -x509 -days 365 -nodes -out certs/$DOMAIN.cert -keyout certs/$DOMAIN.key -subj '/C=ES/ST=Madrid/L=Madrid/O=IT/CN=*.'$DOMAIN
}

function create_cert() {
    DOMAIN=$1
    if [ ! -f "certs/$DOMAIN.cert" ]; then
        openssl req -new -x509 -days 365 -nodes -out certs/$DOMAIN.cert -keyout certs/$DOMAIN.key -subj "/C=ES/ST=Madrid/L=Madrid/O=IT/CN=$DOMAIN"
    fi
    echo "[[tls.certificates]]
  certFile = \"/config/certs/$DOMAIN.cert\"
  keyFile  = \"/config/certs/$DOMAIN.key\"
">>traefik-certs.toml
}

function main() {
    rm -rf traefik-certs.toml
#    create_main_cert demo-app.com
    create_cert web.demo-app.com
    create_cert prometheus.demo-app.com
    create_cert grafana.demo-app.com
    create_cert traefik.demo-app.com
}

main "${@}"
