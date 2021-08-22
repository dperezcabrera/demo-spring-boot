#! /bin/bash

cd `dirname $0`/../..

kubectl create namespace argocd

kubectl apply -n argocd -f https://raw.githubusercontent.com/argoproj/argo-cd/stable/manifests/install.yaml

echo "user: admin"
echo "password: "
kubectl get pods -n argocd -l app.kubernetes.io/name=argocd-server -o name | cut -d'/' -f 2

#kubectl port-forward svc/argocd-server -n argocd 8081:443

kubectl -n argocd apply -f argocd/config/argocd.yaml


# Reset password https://www.browserling.com/tools/bcrypt
#bcrypt(password)=$2a$10$rRyBsGSHK6.uc8fntPwVIuLVHgsAhAX7TcdrqW/RADU0uh7CaChLa
#kubectl -n argocd patch secret argocd-secret \
#  -p '{"stringData": {
#    "admin.password": "$2a$10$rRyBsGSHK6.uc8fntPwVIuLVHgsAhAX7TcdrqW/RADU0uh7CaChLa",
#    "admin.passwordMtime": "'$(date +%FT%T%Z)'"
#  }}'