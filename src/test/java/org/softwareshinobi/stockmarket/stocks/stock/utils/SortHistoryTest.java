package org.softwareshinobi.stockmarket.stocks.stock.utils;

import digital.softwareshinobi.napkinexchange.stocks.stock.utils.SortHistory;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.ZonedDateTime;
import java.util.ArrayList;

import digital.softwareshinobi.napkinexchange.stocks.stock.entity.Stock;
import digital.softwareshinobi.napkinexchange.stocks.stock.entity.StockPriceHistory;
import digital.softwareshinobi.napkinexchange.stocks.stock.entity.idclass.StockPriceHistoryId;
import org.junit.jupiter.api.Test;

class SortHistoryTest {

    private static final ArrayList<StockPriceHistory> STOCK_PRICE_HISTORY_LIST_1 = new ArrayList<>();

    @Test
    void stockHistorySorted() {
        populateStockHistoryList();
        SortHistory.sortStockHistoryByDate(STOCK_PRICE_HISTORY_LIST_1);
        assertEquals("1/1/1", STOCK_PRICE_HISTORY_LIST_1.get(0).getId().getMarketDate());
        assertEquals("1/3/1", STOCK_PRICE_HISTORY_LIST_1.get(1).getId().getMarketDate());
        assertEquals("1/5/1", STOCK_PRICE_HISTORY_LIST_1.get(2).getId().getMarketDate());
        assertEquals("2/1/1", STOCK_PRICE_HISTORY_LIST_1.get(3).getId().getMarketDate());
    }

    private static void populateStockHistoryList() {
        STOCK_PRICE_HISTORY_LIST_1.add(new StockPriceHistory(new StockPriceHistoryId(ZonedDateTime.now(),"1"),new Stock(),0.0));
        STOCK_PRICE_HISTORY_LIST_1.add(new StockPriceHistory(new StockPriceHistoryId(ZonedDateTime.now(),"1"),new Stock(),0.0));
        STOCK_PRICE_HISTORY_LIST_1.add(new StockPriceHistory(new StockPriceHistoryId(ZonedDateTime.now(),"1"),new Stock(),0.0));
        STOCK_PRICE_HISTORY_LIST_1.add(new StockPriceHistory(new StockPriceHistoryId(ZonedDateTime.now(),"1"),new Stock(),0.0));
    }

}