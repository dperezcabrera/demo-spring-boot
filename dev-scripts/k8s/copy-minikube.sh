#! /bin/bash

# cd "$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )/../../"


scp -i $(minikube ssh-key) "${@}" docker@$(minikube ip):/home/docker/demo-app
