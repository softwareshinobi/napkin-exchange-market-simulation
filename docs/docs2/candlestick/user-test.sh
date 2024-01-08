##

set -e
set -x 

echo;echo "printing user list"

curl -X GET http://localhost:9999/account

##

echo;echo "creating sasuke3 user"

curl -X POST -H "Content-Type: application/json" -d '{"username":"sasuke3","email":"linuxize@example.com"}' http://localhost:9999/account/create

##

echo;echo "printing user list"

curl -X GET http://localhost:9999/account

##

echo;echo "print sasuke3 detail"

curl -X GET http://localhost:9999/account/sasuke3

echo;echo  "giving sasuke3 a milli"

curl -X POST -H "Content-Type: application/json" -d '{"username":"sasuke3","amountToAdd":1000000}' http://localhost:9999/account/deposit

echo;echo "print sasuke3 detail"

curl -X GET http://localhost:9999/account/sasuke3

##

echo;echo "buy 1000 units of PANDORA"

curl -X POST -H "Content-Type: application/json" -d '{"username":"sasuke3","ticker":"PANDORA","sharesToBuy":1000}' http://localhost:9999/api/v1/inventory/buy/market

echo;echo "print sasuke3 detail"

curl -X GET http://localhost:9999/account/sasuke3

##

sleep 8

echo;echo "sell some pandora"

curl -X POST -H "Content-Type: application/json" -d '{"username":"sasuke3","ticker":"PANDORA","sharesToSell":253}' http://localhost:9999/api/v1/inventory/sell

echo;echo "print sasuke3 detail"

curl -X GET http://localhost:9999/account/sasuke3


echo;echo "print sasuke3 account inventory"

curl -X GET http://localhost:9999/account/sasuke3

echo;echo "print leaderboard"

curl -X GET http://localhost:9999/api/v1/leaderboard
