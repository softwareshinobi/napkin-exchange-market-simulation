#!/bin/bash

set -e

set -x

reset

clear

##

echo "##"
echo "## napkin exchange [production] starting..."
echo "##"

##

docker-compose -f production.yaml pull

docker-compose -f production.yaml build

docker-compose -f production.yaml down --remove-orphans

docker-compose -f production.yaml up -d

##

echo "##"
echo "## napkin exchange [production] started..."
echo "##"

