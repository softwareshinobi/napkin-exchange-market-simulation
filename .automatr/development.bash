reset

clear

cd ..

docker-compose -f development.yaml down --remove-orphans

docker-compose -f development.yaml up --build
