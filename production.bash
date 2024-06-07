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

docker-compose pull

docker-compose build

docker-compose down --remove-orphans

docker-compose up -d

##

echo "##"
echo "## napkin exchange [production] started..."
echo "##"

