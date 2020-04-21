#!/usr/bin/env bash

set -euo pipefail

docker run --rm -u gradle -v "$PWD":/home/gradle/project -w /home/gradle/project gradle gradle test
