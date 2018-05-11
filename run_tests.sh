#!/bin/bash

. bash_functions
rm -f Test_automation_report.xls
mkdir -p report

echo $environment
echo $browser
echo $tag

gradle clean test -i -Pdriver_type="$browser" -Ptest_env=$environment -Pbuild_tag=$tag
