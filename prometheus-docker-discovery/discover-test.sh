#! /bin/bash

docker build . -t dperezcabrera/prometheus-docker-discovery

docker run --rm -it \
	--volume "/var/run/docker.sock:/var/run/docker.sock:ro" \
	--volume "prometheus-discovery:/prometheus/discovery" \
	--name prometheus-discovery \
	dperezcabrera/prometheus-docker-discovery
