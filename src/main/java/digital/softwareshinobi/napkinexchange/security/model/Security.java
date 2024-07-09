package digital.softwareshinobi.napkinexchange.security.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import digital.softwareshinobi.napkinexchange.leaderboard.service.LeaderboardService;
import digital.softwareshinobi.napkinexchange.market.utility.RandomNumberGenerator;
import digital.softwareshinobi.napkinexchange.security.defaults.DefaultStockPrices;
import digital.softwareshinobi.napkinexchange.security.enums.InvestorRating;
import digital.softwareshinobi.napkinexchange.security.enums.MarketCap;
import digital.softwareshinobi.napkinexchange.security.enums.Volatility;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.slf4j.LoggerFactory;

@Entity(name = "stock")
@Table(name = "stock")
@Getter
@Setter
@Inheritance(strategy = InheritanceType.JOINED)
@NoArgsConstructor
public class Security implements Serializable {

  @Transient
   private final org.slf4j.Logger logger = LoggerFactory.getLogger(Security.class);

    @Id
    private String ticker;

    @Column(name = "name")
    private String companyName;

    @Column(name = "sector")
    private String sector;

    @Column(name = "cap")
    @Enumerated(EnumType.STRING)
    private MarketCap marketCap;

    @Column(name = "price")
    private Double price;

    @Column(name = "last_day_price")
    private Double lastDayPrice;

    @Column(name = "momentum")
    private Integer momentum;

    @Column(name = "momentum_streak")
    private Integer momentumStreakInDays;

    @Column(name = "volatile")
    @Enumerated(EnumType.STRING)
    private Volatility volatility;

//    @Column(name = "investor_rating")
//    @Enumerated(EnumType.STRING)
//    private InvestorRating investorRating;
    @OneToMany(mappedBy = "security", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonIgnore
    private List<SecurityPricingHistory> securityPricingHistory = new ArrayList();

    public Security(String ticker,
            String companyName,
            String sector,
            MarketCap marketCap,
            Volatility volatileStock,
            InvestorRating investorRating) {

        this.ticker = ticker;

        this.companyName = companyName;

        this.sector = sector;

        this.marketCap = marketCap;

        this.volatility = volatileStock;

//        this.investorRating = investorRating;
        this.price = DefaultStockPrices.getDefaultPriceWithCap(marketCap);

        this.lastDayPrice = DefaultStockPrices.getDefaultPriceWithCap(marketCap);

        this.momentum = 0;

        this.momentumStreakInDays = 0;

    }

    public void updatePriceWithFormula() {

        double currentSecurityPrice = this.getPrice();

        ////
//@todo move into the class itself and make final
        final RandomNumberGenerator randomNumberGenerator = new RandomNumberGenerator();

        double randomNumber = randomNumberGenerator.generateRandomNumberForSecurity(this.marketCap);

        ////
        double randomPositiveNumber = randomNumberGenerator.getRandomPositiveNumberForStocks(this.marketCap);

        ////
        int theMomentum = this.getMomentum();

////
        double theVolatility = this.getVolatility().ordinal();

////
        double newSecurityPrice = Math.round(
                (currentSecurityPrice
                + (currentSecurityPrice * randomNumber)
                + (currentSecurityPrice * (randomNumber * theVolatility))
                // + (this.getInvestorRating().investorRatingMultiplier() * randomPositiveNumber)
                + (theMomentum * randomPositiveNumber)) * 100.00) / 100.00;

        if (this.ticker.equalsIgnoreCase("callisto")) {
//            logger.debug();
//            logger.debug();
//            logger.debug("//// enter //// ");
//            logger.debug("symbol / " + ticker);
//
//            logger.debug("current price / " + currentSecurityPrice);
//            logger.debug("randomNumber / " + randomNumber);
//            logger.debug("randomPositiveNumber / " + randomPositiveNumber);
//            logger.debug("theMomentum / " + theMomentum);
//            logger.debug("theVolatility / " + theVolatility);
//            logger.debug("newSecurityPrice / " + newSecurityPrice);
//            logger.debug("//// exit //// ");
//            logger.debug();
//            logger.debug();
        }

        this.setPrice(newSecurityPrice);
        ////
//        randomNumberGenerator = null;

    }

//    private void updatePriceWithFormulaHack() {
//
//        //Volatile stocks change twice to increase market movements
//        double randomNumber = GetRandomNumber.getRandomNumberForStocks(this.marketCap);
//
//        double randomPositiveNumber = GetRandomNumber.getRandomPositiveNumberForStocks(this.marketCap);
//
//        double stockPrice = this.getPrice();
//
//        double newPrice = Math.round((stockPrice
//                + (stockPrice * randomNumber)
//                + (stockPrice * (randomNumber * this.getVolatileStock().ordinal()))
//                + (this.getInvestorRating().investorRatingMultiplier() * randomPositiveNumber)
//                + (this.getMomentum() * randomPositiveNumber)) * 100.00) / 100.00;
//
//        setPrice(newPrice + 4.0);
//
//    }
    public void updateMomentum() {

        int momentumStreak = getMomentumStreakInDays();

        if (momentumStreak >= 3) {

            setMomentum(1);

            return;

        }

        if (momentumStreak <= -3) {

            setMomentum(-1);

            return;

        }

        setMomentum(0);

    }

    public void updateMomentumStreak() {

        if (getMomentumStreakInDays() == null) {

            setMomentumStreakInDays(0);

            return;

        }

        double price = getPrice();

        int momentumStreakDays = getMomentumStreakInDays();

        if (price > getLastDayPrice()) {

            if (momentumStreakDays <= -1) {

                setMomentumStreakInDays(0);

                return;

            }

            setMomentumStreakInDays(momentumStreakDays + 1);

            return;

        }

        if (price < getLastDayPrice()) {

            if (getMomentum() >= 1) {

                setMomentumStreakInDays(0);

                return;

            }

            setMomentumStreakInDays(momentumStreakDays - 1);

        }

    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("security / {");
        sb.append("ticker=").append(ticker);
        sb.append(", price=").append(price);
        sb.append(", lastDayPrice=").append(lastDayPrice);
        //sb.append(", priceHistory=").append(priceHistory);
        sb.append('}');
        return sb.toString();
    }
}
