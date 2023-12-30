package digital.softwareshinobi.napkinexchange.stocks.stock.defaults;

import digital.softwareshinobi.napkinexchange.stocks.stock.enums.MarketCap;

import java.util.Random;

public class DefaultStockPrices {

    private static final Random random = new Random();

    //   private static final double DEFAULT = 4.000;
    private static final double MIN_VALUE = -42.0;

    private static final double MAX_VALUE = 42.0;

    private static final double DEFAULT_LARGE_CAP_PRICE = 228.0;

    private static final double DEFAULT_MID_CAP_PRICE = 128.0;

    private static final double DEFAULT_SMALL_CAP_PRICE = 64.0;

    public static double getDefaultPriceWithCap(MarketCap marketCap) {
        // return DEFAULT + random.nextDouble(MIN_VALUE, MAX_VALUE);
        //  

        return switch (marketCap) {

            case Large ->
                DEFAULT_LARGE_CAP_PRICE + random.nextDouble(MIN_VALUE, MAX_VALUE);

            case Mid ->
                DEFAULT_MID_CAP_PRICE + random.nextDouble(MIN_VALUE, MAX_VALUE);

            case Small ->
                DEFAULT_SMALL_CAP_PRICE + random.nextDouble(MIN_VALUE, MAX_VALUE);

        };

    }

}
