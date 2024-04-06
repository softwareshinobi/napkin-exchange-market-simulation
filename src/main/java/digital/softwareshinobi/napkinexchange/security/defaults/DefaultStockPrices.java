package digital.softwareshinobi.napkinexchange.security.defaults;

import digital.softwareshinobi.napkinexchange.security.enums.MarketCap;
import java.util.Random;

public class DefaultStockPrices {

    private static final Random random = new Random();

    private static final double DEFAULT_LARGE_CAP_PRICE = 42d;

    private static final double DEFAULT_MID_CAP_PRICE = 24;

    private static final double DEFAULT_SMALL_CAP_PRICE = 8d;

    public static double getDefaultPriceWithCap(MarketCap marketCap) {

        return switch (marketCap) {

            case Large ->

                DEFAULT_LARGE_CAP_PRICE + random.nextDouble(
                DEFAULT_LARGE_CAP_PRICE * 0.9,
                DEFAULT_LARGE_CAP_PRICE * 1.1
                );

            case Mid ->

                DEFAULT_MID_CAP_PRICE + random.nextDouble(
                DEFAULT_MID_CAP_PRICE * .08,
                DEFAULT_MID_CAP_PRICE * 1.1
                );

            case Small ->

                DEFAULT_SMALL_CAP_PRICE + random.nextDouble(
                DEFAULT_SMALL_CAP_PRICE * .08,
                DEFAULT_SMALL_CAP_PRICE * 1.1
                );

        };

    }

}
