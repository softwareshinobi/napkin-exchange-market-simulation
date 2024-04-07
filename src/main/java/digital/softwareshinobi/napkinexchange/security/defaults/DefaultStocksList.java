package digital.softwareshinobi.napkinexchange.security.defaults;

import digital.softwareshinobi.napkinexchange.security.model.Security;
import digital.softwareshinobi.napkinexchange.security.enums.InvestorRating;
import digital.softwareshinobi.napkinexchange.security.enums.MarketCap;
import digital.softwareshinobi.napkinexchange.security.enums.Volatility;
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

    private static final List<Security> allStocks = List.of(new Security("PANDORA", "Pandora Holdings", "Content Creation", MarketCap.Large, Volatility.VOLATILE, InvestorRating.Buy),
            new Security("DIONE", "The Dione Group", "Content Creation", MarketCap.Small, Volatility.VOLATILE, InvestorRating.Buy),
            new Security("CALLISTO", "Callisto Industrials", "Content Creation", MarketCap.Mid, Volatility.VOLATILE, InvestorRating.Buy),
            new Security("EUROPA", "Europa X", "Content Creation", MarketCap.Mid, Volatility.VOLATILE, InvestorRating.Buy)
    );

    public static int getCountForDefaultStocks() {
        return allStocks.size();
    }

    public static List<Security> getAllDefaultStocks() {
        return allStocks;
    }
}
