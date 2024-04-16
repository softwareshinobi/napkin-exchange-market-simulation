package digital.softwareshinobi.napkinexchange.market.service;

import digital.softwareshinobi.napkinexchange.market.model.Market;
import digital.softwareshinobi.napkinexchange.security.model.Security;
import digital.softwareshinobi.napkinexchange.security.service.SecurityPricingHistoryService;
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
    private final SecurityService stockService;

    @Autowired
    private final MarketService marketService;

    @Autowired
    private final SecurityPricingHistoryService stockPriceHistoryService;

    public ZonedDateTime tickMarket() {

        Market market = marketService.getMarket();

        market.tick();

        this.marketService.saveMarket(market);

        ZonedDateTime currentMarketTime = market.getDate();

        market = null;

        return currentMarketTime;

    }

    public ZonedDateTime executeMarketTickActivities() {

        this.updateStocksOnTick();

        ZonedDateTime marketDate = this.getMarketTime();

        return marketDate;

    }

    public void updateStocksOnTick() {

        List<Security> stocks = this.stockService.getAllSecurities();

        stocks.forEach(stock -> {

            stock.updatePriceWithFormula();

            stock.updateMomentumStreak();

            stock.updateMomentum();

            stock.setLastDayPrice(stock.getPrice());

        });

        this.stockService.updateAllStocksInDatabase(stocks);

        stocks = null;
    }

    public ZonedDateTime getMarketTime() {

        Market market = this.marketService.getMarket();

        ZonedDateTime currentMarketTime = market.getDate();

        market = null;

        return currentMarketTime;

    }

}
