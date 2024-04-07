#!/bin/sh

##

reset

clear

##

echo
echo "stopping the services"
echo

/bin/sh ./stop-docker-compose-services.sh

##

echo
echo "removing the volume data"
echo

sudo rm -rf ./volume-data/

## 

echo
echo "pruning inactive Docker content"
echo

docker system prune -f

## 

echo
echo "finished purging all created content"
echo
