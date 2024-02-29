package digital.softwareshinobi.napkinexchange.trader.utility;

import digital.softwareshinobi.napkinexchange.trader.model.StockOwned;

import java.util.Set;

/*
    Find the account owned stocks based on specific fields
 */
public class FindStockOwned {

    public static StockOwned findOwnedStockByTicker(Set<StockOwned> stocksOwned, String ticker) {
        return stocksOwned.stream()
                .findFirst()
                .filter(stock -> stock.getTicker().equalsIgnoreCase(ticker))
                .orElse(null);
    }
}
