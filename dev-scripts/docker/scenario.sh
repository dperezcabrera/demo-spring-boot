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

ICON_PATH="$PWD/dev-scripts/.icons/server.png"


# Actions
# -------

function _checkScenario() {
    if [ ! -f "test/scenarios/$1/docker-compose.yml" ]; then
        echo "Error: No se ha definido el escenario $1"
        exit 1
    fi
}

function up() {
    _checkScenario "$1"
    docker-compose -f "test/scenarios/$1/docker-compose.yml" up
}

function down() {
    _checkScenario "$1"
    docker-compose -f "test/scenarios/$1/docker-compose.yml" down
}

function test() {
    _checkScenario "$1"
    mvn -Pe2e-test verify -DScenarios="scenario-$1" -De2e-config-file=test/e2e-config.properties
}

function debug() {
    _checkScenario "$1"
    mvn -Pe2e-test verify -DScenarios="scenario-$1" -De2e-config-file=test/e2e-config.properties -Ddebug=true
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
