#! /bin/bash

cd "$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )/../../"

WORK_DIR=demo-app

function copy_files_minikube() {
    scp -i $(minikube ssh-key) Dockerfile docker@$(minikube ip):"$WORK_DIR/"
    scp -i $(minikube ssh-key) target/*.jar docker@$(minikube ip):"$WORK_DIR/target/"
#   scp -i $(minikube ssh-key) config/docker/application.yaml docker@$(minikube ip):"$WORK_DIR/config/docker/"
}

function build_docker_minikube() {
    ssh -tt -i $(minikube ssh-key) docker@$(minikube ip) << EOF
        cd "$WORK_DIR"
        docker build . -t dperezcabrera/demo-spring-boot:0.0.1
        exit
EOF
}

function restart_pods() {
    kubectl delete pods -l service=demo-spring-boot
}

function clean_images_minikube(){
    ssh -tt -i $(minikube ssh-key) docker@$(minikube ip) << EOF
        docker rmi `docker images | grep none | tr -s " " | cut -f3 -d" "`
        exit
EOF
}

function main() {
    copy_files_minikube
    build_docker_minikube
    restart_pods
    clean_images_minikube
}

main "${@}"
