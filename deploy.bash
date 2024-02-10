
docker-compose down --remove-orphans

rm -rf .container-volumes/

git pull;

docker-compose up
