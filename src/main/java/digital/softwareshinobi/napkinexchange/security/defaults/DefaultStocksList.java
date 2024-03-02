package digital.softwareshinobi.napkinexchange.security.defaults;

import digital.softwareshinobi.napkinexchange.security.enums.InvestorRating;
import digital.softwareshinobi.napkinexchange.security.enums.MarketCap;
import digital.softwareshinobi.napkinexchange.security.enums.Volatility;
import digital.softwareshinobi.napkinexchange.security.entity.Stock;

import java.util.List;

/*
    A list of default stocks providing the baseline data for each
    * Ticker Symbol
    * Name
    * Sector
    * Market Cap
    * Volatile or not
 */
public class DefaultStocksList {

    private static final List<Stock> allStocks = List.of(
            new Stock("PANDORA", "Pandora Holdings", "Content Creation", MarketCap.Small, Volatility.VOLATILE, InvestorRating.Buy),
            new Stock("DIONE", "The Dione Group", "Content Creation", MarketCap.Mid, Volatility.VOLATILE, InvestorRating.Buy),
            new Stock("CALLISTO", "Callisto Industrials", "Content Creation", MarketCap.Large, Volatility.VOLATILE, InvestorRating.Buy),
            new Stock("EUROPA", "Europa X", "Content Creation", MarketCap.Mid, Volatility.VOLATILE, InvestorRating.Buy)
    );

    public static int getCountForDefaultStocks() {
        return allStocks.size();
    }

    public static List<Stock> getAllDefaultStocks() {
        return allStocks;
    }
}
