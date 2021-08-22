#!/bin/bash

NETWORK=demo-network
if [ `docker network ls |tr -s " " | cut -f2 -d" " | grep -e ^${NETWORK}$ | wc -l` -eq 0 ]; then
    docker network create ${NETWORK}
fi
