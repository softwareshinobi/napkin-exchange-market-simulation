reset

clear

set -e 

set -x

##

#echo "setting a few shares for a stop loss situation"
#/buy/limit

curl -X POST -H "Content-Type: application/json" -d '{"username":"SasukeReloaded","ticker":"DIONE","sharesToBuy":10000,"limitOrderType":"limit-order-sell-stop-loss","limitPrice":2500.99}' http://localhost:9999/inventory/sell/stop-loss