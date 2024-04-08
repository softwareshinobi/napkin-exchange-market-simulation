#!/bin/sh

##

reset

clear

##

echo
echo "stopping the services"
echo

docker-compose down --remove-orphans

##

echo
echo "services stopped"
echo