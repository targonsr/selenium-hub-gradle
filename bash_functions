function test_env_not_specified {
    echo "Bad test environment parameter name or not specified. Exiting"
    exit 1
}

function testenv_set {
    if [[ "$1" == "test" ]] || [[ "$1" == "prep" ]];then
        echo $1
    else
        test_env_not_specified
    fi
}

function run_environment {
    if [[ "$1" == "local" ]]; then
        echo "local"
    elif [[ "$1" == "remote" ]]; then
        echo "remote"
    else
        "Bad runtime environment name"
        exit 1
    fi
}

function browser_set {
    if [[ "$1" == "firefox" ]];then
        echo "firefox"
    elif [[ "$1" == "chrome" ]]; then
        echo "chrome"
    elif [[ "$1" == "both" ]]; then
        echo "both"
    else
        echo "Bad browser name or browser not supported yet"
        exit 1
    fi
}

function build_tag_set {
    if [[ -z "$1" ]];then
        echo $(date +%d%m%y%H%M)
    else
        echo $1
    fi
}

function rm_env_file_if_exist {
    if [[ -e compose.env ]]; then
        rm compose.env
    fi
}

function generate_env_file {
    #Parameters order:
    # $1 browser
    # $2 environment
    # $3 build tag

    if [[ -z "$3" ]]; then
        echo "There are no correct parameters amount to generate compose.env file!!"
        exit 1
    fi

    export browser=$1
    export environment=$2
    export tag=$3

    rm_env_file_if_exist
    envsubst < "compose.env.template" > "compose.env"

}

#Parameters order:
# $1 runtime environment
# $2 tests environment
# $3 debug
# $4 browser
# $5 brakeFlag
# #6 tag
function run_tests {
    pwd
    generate_env_file $4 $2 $6
    if [[ "$1" == "remote" ]]; then
        if $3; then
            docker-compose -f docker-compose-debug.yml -p "$6" up --build --abort-on-container-exit
        else
            docker-compose -p "$6" up --build --abort-on-container-exit
        fi
            docker-compose -p "$6" down
    elif [[ "$1" ==  "local" ]]; then

        if $3; then
            echo "No debug mode in local tests run. Running in normal mode"
        fi
        ./docker_tests_execution.sh "$2" "$4" $5 "$1"
    fi
}
