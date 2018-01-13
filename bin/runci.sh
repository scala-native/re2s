#!/usr/bin/env bash

set -eux
TEST=${1}

case "$TEST" in
  "scalafmt" )
    ./scalafmt --test
    ;;
  * )
    sbt $TEST
    ;;
esac
