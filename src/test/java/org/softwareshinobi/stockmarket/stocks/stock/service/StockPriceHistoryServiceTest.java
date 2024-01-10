package org.softwareshinobi.stockmarket.stocks.stock.service;

import digital.softwareshinobi.napkinexchange.ticker.service.StockPriceHistoryService;
import digital.softwareshinobi.napkinexchange.ticker.entity.StockPriceHistory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
class StockPriceHistoryServiceTest {

    @Autowired
    private StockPriceHistoryService stockPriceHistoryService;

    @Test
    void stockHistoryOnlyHappensOnThirdMonths(){
        List<StockPriceHistory> stockPriceHistory = stockPriceHistoryService.findStockHistoryByTicker("GOOG");
        stockPriceHistory.forEach(Assertions::assertNotNull);
    }

}