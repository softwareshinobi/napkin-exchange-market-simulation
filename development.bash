#!/bin/bash

##

reset

clear

##

set -e

set -x

##

docker-compose -f development.yaml down --remove-orphans

docker-compose -f development.yaml up --build 

##

docker stop napkin-exchange-market-phpmyadmin

#docker stop napkin-exchange-market-simulation

docker stats