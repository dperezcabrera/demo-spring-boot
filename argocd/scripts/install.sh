#! /bin/bash

cd `dirname "$0"`/../..

kubectl create namespace argocd

kubectl apply -n argocd -f https://raw.githubusercontent.com/argoproj/argo-cd/stable/manifests/install.yaml

echo "user: admin"
echo "password: password"
# Reset password https://www.browserling.com/tools/bcrypt
#bcrypt(password)=$2a$10$rRyBsGSHK6.uc8fntPwVIuLVHgsAhAX7TcdrqW/RADU0uh7CaChLa
kubectl -n argocd patch secret argocd-secret \
  -p '{"stringData": {
    "admin.password": "$2a$10$rRyBsGSHK6.uc8fntPwVIuLVHgsAhAX7TcdrqW/RADU0uh7CaChLa",
    "admin.passwordMtime": "'$(date +%FT%T%Z)'"
  }}'

kubectl -n argocd apply -f argocd/config/argocd.yaml

kubectl apply -f argocd/scripts/demo-ingress.yaml

kubectl port-forward svc/argocd-server -n argocd 8081:443