#! /bin/bash

cd "$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )/../../config/k8s"

function create_cert() {
    DOMAIN=$1
    openssl req -new -x509 -days 365 -nodes -out certs/$DOMAIN.crt -keyout certs/$DOMAIN.key -subj "/C=ES/ST=Madrid/L=Madrid/O=IT/CN=$DOMAIN"
}

function create_secret() {
    SECRET_NAME=$1
    DOMAIN=$2
    kubectl create secret generic $SECRET_NAME --from-file=certs/$DOMAIN.key --from-file=certs/$DOMAIN.crt
}

function main() {
    cat demo-ingress.yaml | grep host | rev | cut -f1 -d" " | rev | while read line; do
        create_cert $line
        create_secret demo-ssl-secrets $line
    done
    kubectl get secret demo-ssl-secrets -o yaml > demo-ssl-secrets.yaml
}

main "${@}"