#! /bin/sh

INTERVAL_CHECK=${INTERVAL_CHECK:-15}
TARGETS_DIRECTORY=${TARGETS_DIRECTORY:-/prometheus/discovery}
TARGET_FILE_PATH="$(echo $TARGETS_DIRECTORY | sed 's/\/$//g' )/targets.json"

prepare() {
    if [ ! -d "$TARGETS_DIRECTORY" ]; then
        mkdir -p "$TARGETS_DIRECTORY"
    fi

    if [ ! -f "$TARGET_FILE_PATH" ]; then
        echo '{}'> "$TARGET_FILE_PATH"
    fi
}

check_errors() {
    cat $1 | jq -e . >/dev/null 2>&1 | wc -l
}

search() {
    echo '[' >targets.json.tmp
    docker ps --filter "label=prometheus-discovery.enabled=true" | tail -n +2 | rev | cut -f1 -d" " | rev | while read container; do
        echo '{"targets": [ ' >>targets.json.tmp
        docker inspect $container --format '{{ index .Config.Labels "prometheus-discovery.target" }}' | sed "s/ /\",\"/g" | sed 's/^/"/;s/$/"/' >>targets.json.tmp
        echo  '], "labels": {' >>targets.json.tmp
        docker inspect $container --format '{{ index .Config.Labels "prometheus-discovery.labels" }}' | tr ',' '\n' | sed 's/=/":"/g' | sed 's/^/"/;s/$/"/' | tr '\n' ',' | rev | cut -c2- | rev >>targets.json.tmp
        echo  '}},' >>targets.json.tmp
    done
    echo  ']' >>targets.json.tmp

    cat targets.json.tmp | tr "\n" " " | sed 's/}, ]/}]/g' >targets.json.tmp2

    ERRORS=`check_errors targets.json.tmp2`

    if [ "$ERRORS" -eq "0" ]; then
        if [ `diff targets.json.tmp2 "$TARGET_FILE_PATH" | wc -l` -ne 0 ]; then
            mv targets.json.tmp2 "$TARGET_FILE_PATH"
        fi
        sleep $INTERVAL_CHECK
    fi
}

main() {
    prepare
    while [ true ]; do
        search
    done
}

main
