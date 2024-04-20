package digital.softwareshinobi.napkinexchange.market.service;

import digital.softwareshinobi.napkinexchange.market.enums.MarketTrajectory;
import digital.softwareshinobi.napkinexchange.market.model.Market;
import digital.softwareshinobi.napkinexchange.market.repository.MarketRepository;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class MarketService {

    @Autowired
    private final MarketRepository marketRepository;

    public ZonedDateTime systemTime() {

        return this.getMarket().getDate();

    }

    public Market getMarket() {
        System.out.println("getMarketgetMarketgetMarketgetMarketgetMarket");

        Market market = this.marketRepository.findById(1).orElse(null);

        System.out.println("market / " + market);

        if (market == null) {

            System.out.println("was n7ull");

            LocalDate newYearsDayYear3005 = LocalDate.of(3005, 1, 1);

            ZoneId bogotaTimeZone = ZoneId.of("America/Bogota");

            LocalTime openingTime = LocalTime.of(8, 0);

            var startingTime = ZonedDateTime.of(newYearsDayYear3005, openingTime, bogotaTimeZone);

            market = new Market(startingTime, 72.0, MarketTrajectory.BULL);

            this.saveMarket(market);

        }

        System.out.println("Market / " + market);
        System.exit(34);
        return market;

    }

    public void saveMarket(final Market market) {

        if (market == null) {

            return;

        }

        this.marketRepository.save(market);

    }

}
