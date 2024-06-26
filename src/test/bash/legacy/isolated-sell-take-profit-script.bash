reset

clear

set -e 

set -x

##

#echo "setting a few shares for a take profit situation"

curl -X POST -H "Content-Type: application/json" -d '{"username":"SasukeReloaded","ticker":"PANDORA","sharesToBuy":2044,"limitOrderType":"limit-order-sell-take-profit","limitPrice":2000.00}' http://localhost:9999/inventory/sell/take-profit
