package digital.softwareshinobi.napkinexchange.security.service;

import lombok.AllArgsConstructor;
import digital.softwareshinobi.napkinexchange.market.model.Market;
import digital.softwareshinobi.napkinexchange.market.service.MarketService;
import digital.softwareshinobi.napkinexchange.security.model.StockPriceHistory;
import digital.softwareshinobi.napkinexchange.security.model.StockPriceHistoryId;
import digital.softwareshinobi.napkinexchange.security.repository.StockPriceHistoryRepository;
import digital.softwareshinobi.napkinexchange.security.utils.SortHistory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class StockPriceHistoryService {

    @Autowired
    private final StockPriceHistoryRepository stockPriceHistoryRepository;
    @Autowired
    private final StockService stockService;
    @Autowired
    private final MarketService marketService;

    public void saveStockHistoryDaily() {

        Market market = marketService.getMarket();

        stockService.getAllStocks().forEach(stock
                -> stockPriceHistoryRepository.save(
                        new StockPriceHistory(
                                new StockPriceHistoryId(market.getDate(), stock.getTicker()),
                                stock,
                                stock.getPrice())));
    }

    public List<StockPriceHistory> findStockHistoryByTicker(String ticker) {
        List<StockPriceHistory> stockPriceHistory = stockPriceHistoryRepository.findAll().stream()
                .filter(history -> history.getStock().getTicker().equalsIgnoreCase(ticker))
                .collect(Collectors.toList());
        SortHistory.sortStockHistoryByDate(stockPriceHistory);
        return stockPriceHistory;
    }

    @Transactional
    public void truncateStockHistoryAtEndOfYear() {
        stockPriceHistoryRepository.truncateTable();
    }
}
