#!/bin/sh

##

reset

clear

##

echo
echo "stopping the services"
echo

docker-compose down --remove-orphans

docker-compose rm -f

##

echo
echo "creating the docker network"
echo

docker network create software-shinobi-net

##

echo
echo "starting the services"
echo

docker-compose up -d

##

echo
echo "printing local container information"
echo

docker ps
