#! /bin/bash

cd `dirname "$0"`/../..

function authenticate() {
    curl -i -H "Content-Type: application/json" -X POST -d '{"username": "admin", "'password'": "1"}' https://demo-app.com/login --insecure 2>/dev/null | grep authorization | cut -f2- -d" "
}

function main() {
    authenticate pepe 1
}

main "${@}"