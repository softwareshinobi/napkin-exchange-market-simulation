package digital.softwareshinobi.napkinexchange.market.service;

import digital.softwareshinobi.napkinexchange.market.model.Market;
import digital.softwareshinobi.napkinexchange.security.model.Security;
import digital.softwareshinobi.napkinexchange.security.service.SecurityHistoryService;
import digital.softwareshinobi.napkinexchange.security.service.SecurityService;
import java.time.ZonedDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class MarketActivityService {

    @Autowired
    private final SecurityService securityService;

    @Autowired
    private final MarketService marketService;

    @Autowired
    private final SecurityHistoryService securityHistoryService;

    public ZonedDateTime getTime() {

        Market market = this.marketService.getMarket();

        ZonedDateTime currentMarketTime = market.getDate();

        market = null;

        return currentMarketTime;

    }

    public ZonedDateTime tick() {

        Market market = this.marketService.getMarket();

        market.tick();

        this.marketService.saveMarket(market);

        this.tickSecurities();

        ////////
        ZonedDateTime currentMarketTime = market.getDate();

        market = null;

        return currentMarketTime;

    }

    private void tickSecurities() {

        List<Security> securityList = this.securityService.getAllSecurities();

        securityList.forEach(security -> {

            security.updatePriceWithFormula();

            security.updateMomentumStreak();

            security.updateMomentum();

            security.setLastDayPrice(security.getPrice());

        });

        this.securityService.updateAllStocksInDatabase(securityList);

        securityList = null;

    }

}
