reset

clear

curl -X POST -H "Content-Type: application/json" -d '{"username":"SasukeReloaded","email":"sasuke@uchiha.com"}' http://localhost:9999/account/create

curl -X POST -H "Content-Type: application/json" -d '{"username":"SasukeReloaded","amountToAdd":10000000}' http://localhost:9999/account/deposit

curl -X POST -H "Content-Type: application/json" -d '{"username":"SasukeReloaded","ticker":"EUROPA","sharesToBuy":1500, "limitPrice":0.99, "limitOrderType":"limit-order-buy-stop"}' http://localhost:9999/inventory/buy/market/auto-close 



curl -X GET http://localhost:9999/account/SasukeReloaded










##curl -X POST -H "Content-Type: application/json" -d '{"username":"SasukeReloaded","ticker":"EUROPA","sharesToBuy":1000, "limitPrice":0.99, "limitOrderType":"limit-order-buy-stop"}' http://localhost:9999/inventory/buy/stop/auto-close 