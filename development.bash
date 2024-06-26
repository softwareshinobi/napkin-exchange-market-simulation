#!/bin/bash

##

reset

clear

##

set -e

set -x

##

docker-compose -f development.yaml down --remove-orphans

docker-compose -f development.yaml up --build -d

docker stop napkin-exchange-market-simulation
