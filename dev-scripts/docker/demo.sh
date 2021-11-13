#! /bin/bash

# Initialization
LOCATION="$(cd "$( dirname "$0" )" && pwd )"
SCRIPT=`echo $0 | rev | cut -f1 -d"/" | rev`
CONFIG_SCRIPT="$LOCATION/.config_$SCRIPT"

# Load configuration
if [ -f "$CONFIG_SCRIPT" ]; then
    . "$CONFIG_SCRIPT"
fi

cd `dirname "$0"`/../..


# Constants

APP_NAME=${APP_NAME:-demo}
ICON_PATH="$PWD/dev-scripts/.icons/server.png"
NETWORK=$APP_NAME-net
WEB_NAME=$APP_NAME-web
WEB_PORT=8080
DEBUG_PORT=8888
ADMIN_DB_PORT=5050
ADMIN_DB_NAME=$APP_NAME-admin-postgres
ADMIN_DB_VOLUME=$APP_NAME-admin-postgres
ADMIN_DB_USER=a@a.a
ADMIN_DB_PASSWORD=1
DB_NAME=$APP_NAME-postgres
DB_PORT=5432
DB_VOLUME=$APP_NAME-postgres
DB_SCHEMA=$APP_NAME
DB_USER=$APP_NAME
DB_PASSWORD=password
JWT_SECRET=11111111111111111111111111111111111111111111111111111111111111111111111111111111111111
TIMEZONE=`timedatectl | grep "Time zone" | cut -f2 -d: | cut -f2 -d" "`

# Actions
# -------

function _create_network() {
    if [ `docker network ls | grep $NETWORK | wc -l` -eq 0 ]; then
        docker network create "$NETWORK"
    fi
}

function _create_volume() {
    volumen=$1
    if [ `docker volume ls | grep $volumen | wc -l` -eq 0 ]; then
        docker volume create "$volumen"
    fi
}

function db_start() {
    _create_network
    _create_volume $DB_VOLUME
    docker run --rm -it \
        --name $DB_NAME \
        --network "$NETWORK" \
        --volume "$DB_VOLUME":/var/lib/postgresql/data \
        --publish $DB_PORT \
        --env TZ="$TIMEZONE" \
        --env POSTGRES_PASSWORD=$DB_PASSWORD \
        --env POSTGRES_DB=$DB_SCHEMA \
        --env POSTGRES_USER=$DB_USER \
        postgres:12-alpine
}

function admin_db_start() {
    _create_network
    _create_volume $ADMIN_DB_VOLUME
    docker run --rm -it \
        --name $ADMIN_DB_NAME \
        --volume "$ADMIN_DB_VOLUME":/var/lib/pgadmin \
        --publish $ADMIN_DB_PORT:80 \
        --network "$NETWORK" \
        --env TZ="$TIMEZONE" \
        --env "PGADMIN_DEFAULT_EMAIL=$ADMIN_DB_USER" \
        --env "PGADMIN_DEFAULT_PASSWORD=$ADMIN_DB_PASSWORD" \
        dpage/pgadmin4
}

function web_login(){
    curl -i -H "Content-Type: application/json" -X POST -d '{"username": "admin", "password": "1"}' http://localhost:8080/api/v1/login 2>/dev/null | grep 'Authorization: '| cut -f2- -d" "
}

function web_start() {
    _create_network
    APP_BIN=`ls target/$APP_NAME*.jar`
    docker run --rm -it \
        --name $WEB_NAME \
        --network "$NETWORK" \
        --publish $WEB_PORT:8080 \
        --env TZ="$TIMEZONE" \
        --env DATABASE_URL="jdbc:postgresql://$DB_NAME:$DB_PORT/$DB_SCHEMA" \
        --env DATABASE_USERNAME="$DB_USER" \
        --env DATABASE_PASSWORD="$DB_PASSWORD" \
        --env DATABASE_PLATFORM="org.hibernate.dialect.PostgreSQLDialect" \
        --env JWT_SECRET="$JWT_SECRET" \
        --volume "$PWD/$APP_BIN:/app/spring-boot-app.jar" \
        --volume "$PWD/config/docker/application.yaml:/app/config/application.yaml" \
        openjdk:15-slim java -jar /app/spring-boot-app.jar --spring.config.location=/app/config/application.yaml
}

function web_debug() {
    _create_network
    APP_BIN=`ls target/$APP_NAME*.jar`
    docker run --rm -it \
        --name $WEB_NAME \
        --network "$NETWORK" \
        --publish $WEB_PORT:8080 \
        --publish $DEBUG_PORT:8888 \
        --env TZ="$TIMEZONE" \
        --env DATABASE_URL="jdbc:postgresql://$DB_NAME:$DB_PORT/$DB_SCHEMA" \
        --env DATABASE_USERNAME="$DB_USER" \
        --env DATABASE_PASSWORD="$DB_PASSWORD" \
        --env DATABASE_PLATFORM="org.hibernate.dialect.PostgreSQLDialect" \
        --volume "$PWD/$APP_BIN:/app/spring-boot-app.jar" \
        --volume "$PWD/config/docker/application.yaml:/app/config/application.yaml" \
        openjdk:15-slim java -Xdebug -Xrunjdwp:transport=dt_socket,server=y,address=8888,suspend=n -jar \
            --spring.config.location=/app/config/application.yaml
}

function admin_db_delete() {
    docker volume rm $ADMIN_DB_VOLUME
}

function admin_db_stop() {
    docker stop $ADMIN_DB_NAME
}

function db_delete() {
    docker volume rm $DB_VOLUME
}

function db_stop() {
    docker stop $DB_NAME
}

function db_backup() {
    docker exec -it $DB_NAME pg_dump --username=$DB_USER --dbname=$DB_SCHEMA >"db_backup_$DB_NAME_"`date +"%Y%m%d%H%M%S"`.sql
}


# Template functions
# ------------------

function shortlist() {
    echo `compgen -A function | grep -v main | grep -v -E "^_.*"`
}

function help() {
    echo "Parametros validos: " `shortlist | tr ' ' '|'`
}

function _actions() {
    echo `compgen -A function | grep -v -E "^(main|menu|autocomplete|help|shortlist)$" | grep -v -E "^_.*"`
}

function _extract_variables() {
    cat "$LOCATION/$SCRIPT" | grep '=${' | while read line; do
        VAR=`echo "$line" | cut -f1 -d"="`
        VAR_EXPAND='${'$VAR':'
        VALUE=`echo "$line" | cut -f2 -d"=" | cut -f1 -d'-'`
        if [[ "$VAR_EXPAND" == "$VALUE" ]] ; then
            echo "$VAR"
        fi
        echo $VARS
    done
}

function config() {
    VARS=`_extract_variables`
    if [ ! -s "$VARS" ]; then
        echo "#! /bin/bash

#Configuration of command '$SCRIPT'">"$CONFIG_SCRIPT"

        for VAR in ${VARS}; do
            read -p "Write $VAR value or enter to keep current value '${!VAR}': " VAR_VALUE
            if [ "$VAR_VALUE" == "" ]; then
                VAR_VALUE="${!VAR}"
            fi
            echo "$VAR=$VAR_VALUE" >>"$CONFIG_SCRIPT"
            
        done
    fi
}

# Bash autocomplete
function autocomplete() {
    cd "$LOCATION"
    echo 'cd "$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

_script()
{
  _script_commands=$(./'$SCRIPT' shortlist)

  local cur prev
  COMPREPLY=()
  cur="${COMP_WORDS[COMP_CWORD]}"
  COMPREPLY=( $(compgen -W "${_script_commands}" -- ${cur}) )

  return 0
}
complete -F _script ./'$SCRIPT'
'>".autocomplete_$SCRIPT"
    echo source "'$LOCATION/.autocomplete_$SCRIPT'"
}

# Ubuntu launcher
function menu() {
    cd "$LOCATION"
    NAME=`echo $SCRIPT | cut -f1 -d"."`
    ACTIONS=`_actions| tr ' ' ';'`
    echo "#!/usr/bin/env xdg-open

[Desktop Entry]
Version=1.0
Type=Application
Terminal=true
Exec=bash -c '\"$LOCATION/$SCRIPT\"'
Name=`echo "${NAME^}" | tr "_" " "`
Icon=$ICON_PATH
Actions=$ACTIONS
">"$NAME.desktop"
    
    for action in `_actions`; do
        echo "[Desktop Action $action]
Exec=bash -c '\"$LOCATION/$SCRIPT\" $action'
Name=`echo "${action^}" | tr "_" " "`
">>"$NAME.desktop"
    done
    mv "$NAME.desktop" ~/.local/share/applications/
}

function main() {
    if [ $# == 0 ]; then
	echo "Error: at least one parameter is required."
        help
	exit 1
    fi
    param="$1"
    shift
    if [ `shortlist | tr " " "\n" | grep -E ^"$param"$ | wc -l` == 1 ]; then
        "$param" $@
    else
        echo "Error: wrong parameter '"$param"'."
	help
        exit 1
    fi
}

main "$@"
