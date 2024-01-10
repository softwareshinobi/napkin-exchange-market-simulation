package org.softwareshinobi.stockmarket.stocks.stock.model.payload;

import digital.softwareshinobi.napkinexchange.ticker.dto.StockDTO;
import digital.softwareshinobi.napkinexchange.ticker.entity.Stock;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StockDTOTest {

    @Test
    void percentChangePositive() {
        //create stock and stock DTO with price values
        Stock stock = new Stock();
        stock.setPrice(100.0);
        stock.setLastDayPrice(50.0);
        StockDTO stockDto = new StockDTO(stock);
        assertEquals(100, stockDto.getPercentChange());

        //create stock and stock DTO with price values
        stock.setPrice(128.11);
        stock.setLastDayPrice(13.243);
        stockDto = new StockDTO(stock);
        assertEquals(867.38, stockDto.getPercentChange());
    }

    @Test
    void percentChangeNegative() {
        //create stock and stock DTO with price values
        Stock stock = new Stock();
        stock.setPrice(50.0);
        stock.setLastDayPrice(100.0);
        StockDTO stockDto = new StockDTO(stock);
        assertEquals(-50, stockDto.getPercentChange());

        //create stock and stock DTO with price values
        stock.setPrice(26.39);
        stock.setLastDayPrice(88.45);
        stockDto = new StockDTO(stock);
        assertEquals(-70.16, stockDto.getPercentChange());
    }

    @Test
    void doesNotThrowArithmeticError() {
        //create stock and stock DTO with price values
        Stock stock = new Stock();
        stock.setPrice(0.0);
        stock.setLastDayPrice(0.0);
        StockDTO stockDto = new StockDTO(stock);
        assertDoesNotThrow(() -> stockDto.getPercentChange(0, 0));
    }

}