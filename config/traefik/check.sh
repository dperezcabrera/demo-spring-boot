#!/bin/bash


curl -i -H "Content-Type: application/json" -X POST -d '{"username": "admin", "'password'": "1"}' https://web.demo-app.com/login --insecure 2>/dev/null | grep authorization | cut -f2- -d" "