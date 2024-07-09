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

        Market market = this.marketRepository.findById(1).orElse(null);

        //    logger.debug("market / " + market);
        if (market == null) {

            LocalDate newYearsDay3005 = LocalDate.of(3005, 1, 1);

            ZoneId bogotaTimeZone = ZoneId.of("America/Bogota");

            LocalTime openingTime = LocalTime.of(8, 0);

            var startingTime = ZonedDateTime.of(newYearsDay3005, openingTime, bogotaTimeZone);

            market = new Market(startingTime, 42d, MarketTrajectory.BULL);

            this.saveMarket(market);

        }

        return market;

    }

    public void saveMarket(final Market market) {

        if (market == null) {

            return;

        }

        this.marketRepository.save(market);

    }

}
