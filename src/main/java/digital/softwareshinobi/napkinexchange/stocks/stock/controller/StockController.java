package digital.softwareshinobi.napkinexchange.stocks.stock.controller;

import lombok.AllArgsConstructor;
import digital.softwareshinobi.napkinexchange.indexfund.utils.Capitalize;
import digital.softwareshinobi.napkinexchange.stocks.stock.dto.StockSummaryDTO;
import digital.softwareshinobi.napkinexchange.stocks.stock.enums.MarketCap;
import digital.softwareshinobi.napkinexchange.stocks.stock.exception.StockNotFoundException;
import digital.softwareshinobi.napkinexchange.stocks.stock.entity.Stock;
import digital.softwareshinobi.napkinexchange.stocks.stock.entity.StockPriceHistory;
import digital.softwareshinobi.napkinexchange.stocks.stock.dto.StockDTO;
import digital.softwareshinobi.napkinexchange.stocks.stock.service.StockPriceHistoryService;
import digital.softwareshinobi.napkinexchange.stocks.stock.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

/*
This controller provides the endpoints related to stocks on the market.
All individual stocks here will be sent using the StockDto class as that class includes
    news, earnings, and price history.
All lists of stocks (such as sorting by sector ar cap) will be sent using the default
stock class, which ignores news, earnings, and price history fields.
 */
@RestController
@RequestMapping(value = "candlestick")
@CrossOrigin
@AllArgsConstructor
@SuppressWarnings("unused")
public class StockController {

    @Autowired
    private StockService stockService;
    @Autowired
    private final StockPriceHistoryService stockPriceHistoryService;

    @GetMapping(value = "/{ticker}")
    public StockDTO getIndividualStockData(@PathVariable String ticker) {
        return new StockDTO(stockService.getStockByTickerSymbol(ticker));
    }

    @GetMapping
    public List<StockSummaryDTO> getAllStockData() {
        return stockService.getAllStocks().stream()
                .map(StockSummaryDTO::new)
                .collect(Collectors.toList());
    }

    @GetMapping(value = "/detailed")
    public List<StockDTO> getAllDetailedStockData() {
        return stockService.getAllStocks().stream()
                .map(StockDTO::new)
                .collect(Collectors.toList());
    }

    @GetMapping(value = "/marketCap/{marketCap}")
    public List<Stock> getAllStocksByMarketCap(@PathVariable String marketCap) {
        MarketCap cap = MarketCap.valueOf(Capitalize.capitalize(marketCap));
        return stockService.getAllStocksByMarketCap(cap);
    }

    @GetMapping(value = "/sector/{sector}")
    public List<Stock> getAllStocksBySector(@PathVariable String sector) {
        return stockService.getAllStocksBySector(sector);
    }

    @GetMapping(value = "/price/{ticker}")
    public double getStockPrice(@PathVariable String ticker) throws StockNotFoundException {
        return stockService.getStockPriceWithTickerSymbol(ticker);
    }

    @GetMapping(value = "/random")
    public StockDTO getRandomStock() {
        Stock stock = stockService.getRandomStock();
        return new StockDTO(stock);
    }

    @RequestMapping(value = "/history/{ticker}")
    public List<StockPriceHistory> getStockHistory(@PathVariable String ticker) {
        return stockPriceHistoryService.findStockHistoryByTicker(ticker);
    }
}
