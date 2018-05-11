#!/bin/bash
. bash_functions

environment=""
runtime_environment=""
tag=""
browser=""
result_file_value=""
debug=false
brakeFlag=false

if [[ "$1" == "--help" ]]; then
    echo "
    [-b <browser> Browser name: firefox, chrome, both]
    [-e <environment> Test environment: test, prep]
    [-t <tag> Tag name]
    [-r <runtime environment> Runtime environment name: local, remote]
    [-d Debug mode. Disabled by default]
    [-f Stop tests on first failure]
    "
    exit 1
fi

while getopts ":b:e:t:r:d:f" opt; do
    case ${opt} in
        b)
            browser=$(browser_set $OPTARG)
            ;;
        e)
            environment=$(testenv_set $OPTARG)
            ;;
        d)
            debug=true
            ;;
        t)
            tag=$(build_tag_set $OPTARG)
            ;;
        r)
            runtime_environment=$(run_environment $OPTARG)
            ;;
        f)
            brakeFlag=true
            ;;
    esac
done

if [[ -z $environment ]]; then
    test_env_not_specified
fi

if [[ -z $runtime_environment ]]; then
    runtime_environment="local"
fi

if [[ -z $browser ]]; then
    echo "Setting default browser: Firefox"
    browser="firefox"
fi

if [[ -z $tag ]]; then
    tag=$(build_tag_set)
fi

rm -rf report build/*
echo "$environment" | awk '{print toupper($0)}'

if [[ "$runtime_environment" == "remote" ]]; then
  docker build -t tests-base -f Dockerfile .
  echo "ENTRYPOINT [\"/opt/automation/docker_tests_execution.sh\", \"$environment\", \"$browser\", \"$brakeFlag\", \"$runtime_environment\"]" >> Dockerfile
  run_tests $runtime_environment $environment $debug $browser $brakeFlag $tag
  sed -i '$d' Dockerfile
fi

if [[ "$runtime_environment" == "local" ]]; then
  run_tests $runtime_environment $environment $debug $browser $brakeFlag $tag
fi

if [[ -e $("pwd")/report/result_file ]]; then
    result_file_value=$(<$("pwd")/report/result_file)
    echo "Tests status: $result_file_value"
fi

rm_env_file_if_exist
exit 0
