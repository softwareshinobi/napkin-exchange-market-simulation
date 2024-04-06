package digital.softwareshinobi.napkinexchange.market.service;

import digital.softwareshinobi.napkinexchange.market.model.Market;
import digital.softwareshinobi.napkinexchange.security.entity.Stock;
import digital.softwareshinobi.napkinexchange.security.service.StockPriceHistoryService;
import digital.softwareshinobi.napkinexchange.security.service.StockService;
import java.time.ZonedDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class MarketActivityService {

    @Autowired
    private final StockService stockService;

    @Autowired
    private final MarketService marketService;

    @Autowired
    private final StockPriceHistoryService stockPriceHistoryService;

    public ZonedDateTime tickMarket() {

        Market market = marketService.getMarket();

        market.tick();

        this.marketService.saveMarket(market);

        ZonedDateTime currentMarketTime = market.getDate();

        return currentMarketTime;

    }

    public ZonedDateTime executeMarketTickActivities() {

        this.updateStocksOnTick();

        ZonedDateTime marketDate = this.getMarketTime();

        return marketDate;

    }

    public void updateStocksOnTick() {

        List<Stock> stocks = this.stockService.getAllStocks();

        stocks.forEach(stock -> {

            stock.updatePriceWithFormula();

            stock.updateMomentumStreak();

            stock.updateMomentum();

            stock.setLastDayPrice(stock.getPrice());

        });

        this.stockService.updateAllStocksInDatabase(stocks);

    }

    public ZonedDateTime getMarketTime() {

        Market market = this.marketService.getMarket();

        ZonedDateTime currentMarketTime = market.getDate();

        return currentMarketTime;

    }

}
