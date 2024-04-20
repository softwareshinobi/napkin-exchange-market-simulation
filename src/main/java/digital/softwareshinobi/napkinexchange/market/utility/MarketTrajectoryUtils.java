package digital.softwareshinobi.napkinexchange.market.utility;

import digital.softwareshinobi.napkinexchange.market.enums.MarketTrajectory;
import digital.softwareshinobi.napkinexchange.market.model.Market;
import digital.softwareshinobi.napkinexchange.security.model.Security;
import java.util.List;

public class MarketTrajectoryUtils {

    public static MarketTrajectory getNewMarketTrajectory(Market market, List<Security> stockList) {

        if (stockList == null || stockList.size() == 0) {
            return MarketTrajectory.NORMAL;
        }

        double stockPricesAverage = stockPricesAverage(stockList);

        double priceChange = (stockPricesAverage / market.getLastMonthAveragePrice()) * 100;

        if (priceChange >= 110) {

            return MarketTrajectory.BULL;

        }

        if (priceChange <= 90) {

            return MarketTrajectory.BEAR;

        }

        return MarketTrajectory.NORMAL;

    }

    public static double stockPricesAverage(List<Security> stockList) {

        return Math.round((stockPricesSum(stockList) / stockList.size()) * 100.00) / 100.00;

    }

    public static double stockPricesSum(List<Security> stockList) {

        double priceSum = 0;

        for (Security stock : stockList) {

            priceSum += stock.getPrice();

        }

        return priceSum;

    }

}
