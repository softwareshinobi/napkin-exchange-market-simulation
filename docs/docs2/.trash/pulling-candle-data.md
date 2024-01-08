# sup candle loadi9n9g.s

## list all known symbols

### request

```
http://localhost:4444/candlestick
```

### response

```
[ {
  "ticker" : "USD/COP",
  "companyName" : "USD/COP",
  "price" : 5.4,
  "lastDayPrice" : 5.49,
  "percentChange" : -1.64
}, {
  "ticker" : "USD/GBP",
  "companyName" : "USD/GBP",
  "price" : 3.7,
  "lastDayPrice" : 3.63,
  "percentChange" : 1.93
}, {
  "ticker" : "USD/HKD",
  "companyName" : "USD/HKD",
  "price" : 4.0,
  "lastDayPrice" : 3.91,
  "percentChange" : 2.3
} ]
```

## see all candles for PANDORA symbol

### request

```
http://localhost:4444/candlestick/PANDORA
```

### response

```json
{
  "ticker" : "PANDORA",
  "companyName" : "Pandora Holdings",
  "sector" : "Content Creation",
  "marketCap" : "Small",
  "price" : 64.3,
  "lastDayPrice" : 64.15,
  "percentChange" : 0.23,
  "momentum" : 0,
  "momentumStreakInDays" : -2,
  "volatileStock" : "VOLATILE",
  "investorRating" : "Buy",
  "newsHistory" : [ ],
  "earningsHistory" : [ ],
  "priceHistory" : [ {
    "marketDate" : "2020-12-31T23:01:00Z",
    "stockPrice" : 64.88
  }, {
    "marketDate" : "2020-12-31T23:02:00Z",
    "stockPrice" : 64.77
  }, {
    "marketDate" : "2020-12-31T23:03:00Z",
    "stockPrice" : 65.65
  }, {
    "marketDate" : "2020-12-31T23:04:00Z",
    "stockPrice" : 65.74
  }, {
    "marketDate" : "2020-12-31T23:05:00Z",
    "stockPrice" : 65.03
  }, {
    "marketDate" : "2020-12-31T23:06:00Z",
    "stockPrice" : 64.6
  }, {
    "marketDate" : "2020-12-31T23:07:00Z",
    "stockPrice" : 64.15
  } ]
}
```
