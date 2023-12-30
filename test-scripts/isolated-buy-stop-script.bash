reset

clear

set -e 

set -x

##

#echo "setting a few shares for a stop loss situation"
#/buy/limit

curl -X POST -H "Content-Type: application/json" -d '{"username":"SasukeReloaded","ticker":"PANDORA","sharesToBuy":444,"limitOrderType":"LONG_SELL_STOP_LOSS","limitPrice":99.99}' http://localhost:9999/inventory/buy/market

