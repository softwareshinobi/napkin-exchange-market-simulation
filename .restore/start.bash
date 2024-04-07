#!/bin/bash

set -e

set -x 

reset

clear

docker-compose rm

docker-compose build

docker-compose down

docker-compose rm

docker-compose up -d
