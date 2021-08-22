#! /bin/bash

cd `dirname "$0"`/../..

function build_docker() {
    docker build . -t dperezcabrera/demo-spring-boot:0.0.1
    docker push dperezcabrera/demo-spring-boot:0.0.1
}


function main() {
    build_docker

}

main "${@}"
