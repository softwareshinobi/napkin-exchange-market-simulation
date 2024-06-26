#!/bin/bash

##

##set -e 

set -x

##

reset

clear

##

curl -X POST -H "Content-Type: application/json" -d '{"username":"sasuke","email":"sasuke@uchiha.com"}' http://localhost:8888/trader

echo

curl -X GET http://localhost:8888/trader/sasuke

echo

curl -X POST -H "Content-Type: application/json" -d '{ "username":"sasuke", "ticker":"EUROPA", "units":666 }' http://localhost:8888/broker/buy/market

echo

curl -X GET http://localhost:8888/trader/sasuke

##########################################

## curl -X POST -H "Content-Type: application/json" -d '{"username":"sasuke","ticker":"EUROPA","sharesToBuy":666, "limitPrice":0.99, "limitOrderType":"limit-order-buy-stop"}' http://localhost:9999/inventory/buy/market/auto-close 

##curl -X POST -H "Content-Type: application/json" -d '{"username":"sasuke","ticker":"EUROPA","sharesToBuy":1000, "limitPrice":0.99, "limitOrderType":"limit-order-buy-stop"}' http://localhost:9999/inventory/buy/stop/auto-close 