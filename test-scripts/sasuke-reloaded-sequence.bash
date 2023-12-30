##

set -e
set -x 

echo;echo "printing user list"

curl -X GET http://localhost:9999/account

##

echo;echo "creating SasukeReloaded user"

curl -X POST -H "Content-Type: application/json" -d '{"username":"SasukeReloaded","email":"sasuke@uchiha.com"}' http://localhost:9999/account/create

##

echo;echo "printing user list"

curl -X GET http://localhost:9999/account

##

echo;echo "print SasukeReloaded detail"

curl -X GET http://localhost:9999/account/SasukeReloaded

echo;echo  "giving SasukeReloaded a milli"

curl -X POST -H "Content-Type: application/json" -d '{"username":"SasukeReloaded","amountToAdd":1000000000}' http://localhost:9999/account/deposit

echo;echo "print SasukeReloaded detail"

curl -X GET http://localhost:9999/account/SasukeReloaded

##

##echo;echo "buy 1000 units of PANDORA @ market price"

curl -X POST -H "Content-Type: application/json" -d '{"username":"SasukeReloaded","ticker":"DIONE","sharesToBuy":100000}' http://localhost:9999/inventory/buy/market

##echo;echo "print SasukeReloaded detail"

##curl -X GET http://localhost:9999/account/SasukeReloaded

##

echo;echo "buy 1000 units of PANDORA @ a high price price"

curl -X POST -H "Content-Type: application/json" -d '{"username":"SasukeReloaded","ticker":"CALLISTO","sharesToBuy":1000, "limitPrice":99.99, "limitOrderType":"limit-order-buy-stop"}' http://localhost:9999/inventory/buy/stop

echo;echo "print SasukeReloaded detail"

curl -X GET http://localhost:9999/account/SasukeReloaded

echo;echo "open limit orders"
curl -X GET http://localhost:9999/inventory/orders/

##

##sleep 8

echo;echo "sell some pandora"

curl -X POST -H "Content-Type: application/json" -d '{"username":"SasukeReloaded","ticker":"CALLISTO","sharesToSell":253}' http://localhost:9999/inventory/sell/market

echo;echo "print SasukeReloaded detail"

curl -X GET http://localhost:9999/account/SasukeReloaded


echo;echo "print SasukeReloaded account inventory"

curl -X GET http://localhost:9999/inventory/orders/SasukeReloaded

echo;echo "print leaderboard"

curl -X GET http://localhost:9999/leaderboard

#########

## self 200 units at a super high price

 curl -X GET http://localhost:9999/inventory/orders/
 
  curl -X POST -H "Content-Type: application/json" -d '{"username":"SasukeReloaded","ticker":"PANDORA","sharesToBuy":1800, "limitPrice":0.99, "limitOrderType":"limit-order-buy-stop"}' http://localhost:9999/inventory/buy/stop
  
  
   curl -X GET http://localhost:9999/inventory/orders/

