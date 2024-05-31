package digital.softwareshinobi.napkinexchange.market.utility;

import digital.softwareshinobi.napkinexchange.security.enums.MarketCap;
import static digital.softwareshinobi.napkinexchange.security.enums.MarketCap.Large;
import static digital.softwareshinobi.napkinexchange.security.enums.MarketCap.Mid;
import static digital.softwareshinobi.napkinexchange.security.enums.MarketCap.Small;
import java.util.Random;

public class RandomNumberGenerator {

    private  final Long randomNumberSeed = 42L;

    private  final Random randomNumberGenerator = new Random(randomNumberSeed);

    public  double generateRandomNumberForSecurity(final MarketCap marketCap) {

        return switch (marketCap) {

            case Large ->

             this.   randomNumberGenerator.nextDouble(-.002, .002);

            case Mid ->

              this.  randomNumberGenerator.nextDouble(-.001, .001);

            case Small ->

              this.  randomNumberGenerator.nextDouble(-.003, .003);

        };

    }

    public  double getRandomPositiveNumberForStocks(final MarketCap marketCap) {

        return switch (marketCap) {

            case Large ->

              this.  randomNumberGenerator.nextDouble(0, .0018);

            case Mid ->

             this.   randomNumberGenerator.nextDouble(0, .0008);

            case Small ->

             this.   randomNumberGenerator.nextDouble(0, .00028);

        };

    }

//    public static double getRandomSmallNumber() {
//        return random.nextDouble(-.001, .001);
//    }
//
//    public static double getRandomSmallPositiveNumber() {
//        return random.nextDouble(0, .0002);
//    }
//
//
//    public static int drawRandomNumberToThirty() {
//    
//        return random.nextInt(30);
//    
//    }
//    
}
