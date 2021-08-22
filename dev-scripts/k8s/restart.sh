#! /bin/bash

function reboot_pods() {
    kubectl rollout restart deployment demo-spring-boot
}

function main() {
    reboot_pods
}

main "${@}"
