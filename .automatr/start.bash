set -e

set -x

cd ..

docker-compose pull

docker-compose build

docker-compose down --remove-orphans

docker-compose up
