#!/usr/bin/env bash

set -eux
TEST=${1}

case "$TEST" in
  "scalafmt" )
    ./bin/scalafmt --test
    ;;
  * )
    sbt $TEST
    ;;
esac
