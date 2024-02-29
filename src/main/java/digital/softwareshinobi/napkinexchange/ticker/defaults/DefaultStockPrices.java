package digital.softwareshinobi.napkinexchange.ticker.defaults;

import digital.softwareshinobi.napkinexchange.ticker.enums.MarketCap;
import java.util.Random;

public class DefaultStockPrices {

    private static final Random random = new Random();

    private static final double DEFAULT_LARGE_CAP_PRICE = 128.32;

    private static final double DEFAULT_MID_CAP_PRICE = 1.0033;

    private static final double DEFAULT_SMALL_CAP_PRICE = 0.8480;

    public static double getDefaultPriceWithCap(MarketCap marketCap) {

        return switch (marketCap) {

            case Large ->

                DEFAULT_LARGE_CAP_PRICE + random.nextDouble(
                DEFAULT_LARGE_CAP_PRICE * .08,
                DEFAULT_LARGE_CAP_PRICE * 1.2
                );

            case Mid ->

                DEFAULT_MID_CAP_PRICE + random.nextDouble(
                DEFAULT_MID_CAP_PRICE * .08,
                DEFAULT_MID_CAP_PRICE * 1.2
                );

            case Small ->

                DEFAULT_SMALL_CAP_PRICE + random.nextDouble(
                DEFAULT_SMALL_CAP_PRICE * .08,
                DEFAULT_SMALL_CAP_PRICE * 1.2
                );

        };

    }

}
