#! /bin/bash

docker run --rm -it \
	--publish 9090:9090 \
	--network demo-network \
	--volume "prometheus-discovery:/discovery/" \
	--volume "$PWD/prometheus.yml:/etc/prometheus/prometheus.yml" \
	--name prometheus \
	prom/prometheus

