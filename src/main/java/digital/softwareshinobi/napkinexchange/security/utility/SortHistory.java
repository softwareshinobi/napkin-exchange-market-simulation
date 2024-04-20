package digital.softwareshinobi.napkinexchange.security.utility;

import digital.softwareshinobi.napkinexchange.security.model.SecurityPricingHistory;
import digital.softwareshinobi.napkinexchange.trader.model.AccountHistory;
import java.util.List;

public class SortHistory {

    public static void sortStockHistoryByDate(List<SecurityPricingHistory> stockPriceHistory) {
        stockPriceHistory.sort((history1, history2) -> {
            return history1.getId().getDateTime().compareTo(history2.getId().getDateTime());
        });
    }

    public static void sortAccountHistoryByDate(List<AccountHistory> accountHistory) {
        accountHistory.sort((history1, history2) -> {
            return history1.getDate().compareTo(history2.getDate());
        });
    }
}
