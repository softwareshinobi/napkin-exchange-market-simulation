set -e

set -x

cd ..

docker-compose build

docker-compose push
