package digital.softwareshinobi.napkinexchange.ticker.utils;

import java.util.List;

import digital.softwareshinobi.napkinexchange.ticker.entity.StockPriceHistory;
import digital.softwareshinobi.napkinexchange.trader.model.entity.AccountHistory;

public class SortHistory {

    public static void sortStockHistoryByDate(List<StockPriceHistory> stockPriceHistory) {
        stockPriceHistory.sort((history1, history2) -> {
            return history1.getId().getMarketDate().compareTo(history2.getId().getMarketDate());
        });
    }

    public static void sortAccountHistoryByDate(List<AccountHistory> accountHistory) {
        accountHistory.sort((history1, history2) -> {
            return history1.getDate().compareTo(history2.getDate());
        });
    }
}
