#!/bin/bash
 . bash_functions

#Parameters order:
# $1 tests environment
# $2 browser
# $3 brakeFlag
# $4 runtime environment

env=""
otherBrowser=""
continueFlag="--continue"
task=""

sleep 3

if $3; then
    continueFlag=""
fi

if [[ ! -z "$1" ]]; then
    env="-Denv=$1"
fi

if [[ "$2" == "firefox" ]]; then
    echo "Tests executing with Firefox browser."
    if [[ ! -z "$4" ]] && [[ "$4" == "remote" ]]; then
        task="remoteFirefoxTest"
    else
        task="firefoxTest"
    fi
    echo "gradle" $continueFlag $env $task
    gradle $continueFlag $env $task
fi

if [[ "$2" == "chrome" ]]; then
    echo "Tests executing with Chrome browser."
    if [[ ! -z "$4" ]] && [[ "$4" == "remote" ]]; then
        task="remoteChromeTest"
    else
        task="chromeTest"
    fi
    echo "gradle" $continueFlag $env $task
    gradle $continueFlag $env $task
fi

if [[ "$2" == "both" ]]; then
    if [[ ! -z "$4" ]] && [[ "$4" == "remote" ]]; then
        task="remoteTest"
    else
        task="test"
    fi
    echo "gradle" $continueFlag $env $task
    gradle $continueFlag $env $task
fi
