##

set -e
set -x 

echo;echo "printing user list"

curl -X GET http://localhost:9999/account

##

echo;echo "creating SasukeReloaded user"

curl -X POST -H "Content-Type: application/json" -d '{"username":"SasukeReloaded","email":"linuxize@example.com"}' http://localhost:9999/account/create

##

echo;echo "printing user list"

curl -X GET http://localhost:9999/account

##

echo;echo "print SasukeReloaded detail"

curl -X GET http://localhost:9999/account/SasukeReloaded

echo;echo  "giving SasukeReloaded a milli"

curl -X POST -H "Content-Type: application/json" -d '{"username":"SasukeReloaded","amountToAdd":1000000}' http://localhost:9999/account/deposit

echo;echo "print SasukeReloaded detail"

curl -X GET http://localhost:9999/account/SasukeReloaded

##

echo;echo "buy 1000 units of PANDORA"

curl -X POST -H "Content-Type: application/json" -d '{"username":"SasukeReloaded","ticker":"PANDORA","sharesToBuy":1000}' http://localhost:9999/api/v1/inventory/buy/market

echo;echo "print SasukeReloaded detail"

curl -X GET http://localhost:9999/account/SasukeReloaded

##

sleep 8

echo;echo "sell some pandora"

curl -X POST -H "Content-Type: application/json" -d '{"username":"SasukeReloaded","ticker":"PANDORA","sharesToSell":253}' http://localhost:9999/api/v1/inventory/sell

echo;echo "print SasukeReloaded detail"

curl -X GET http://localhost:9999/account/SasukeReloaded


echo;echo "print SasukeReloaded account inventory"

curl -X GET http://localhost:9999/account/SasukeReloaded

echo;echo "print leaderboard"

curl -X GET http://localhost:9999/api/v1/leaderboard
