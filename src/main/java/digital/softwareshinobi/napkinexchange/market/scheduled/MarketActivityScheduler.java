package digital.softwareshinobi.napkinexchange.market.scheduled;

import lombok.AllArgsConstructor;
import digital.softwareshinobi.napkinexchange.market.constants.MarketIntervals;
import digital.softwareshinobi.napkinexchange.ticker.service.StockPriceHistoryService;
import digital.softwareshinobi.napkinexchange.trader.service.AccountHistoryService;
import digital.softwareshinobi.napkinexchange.portfolio.controller.LimitOrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
@AllArgsConstructor
public class MarketActivityScheduler {

    private final Logger logger = LoggerFactory.getLogger(MarketActivityScheduler.class);

    @Autowired
    private final HandleMarketActivity handleMarketActivity;

    @Autowired
    private final LimitOrderService limitOrderService;

    @Autowired
    private final AccountHistoryService accountHistoryService;

    @Autowired
    private final StockPriceHistoryService stockPriceHistoryService;

    @Scheduled(fixedRate = MarketIntervals.ONE_SECOND)
    @SuppressWarnings("unused")
    public void dailyMarketActivity() {

        var now = handleMarketActivity.tickMarket();

        logger.info("market time / " + now);

        handleMarketActivity.dailyMarketActivity();

        limitOrderService.processAllLimitOrders();

        accountHistoryService.saveDailyAccountHistory();

        stockPriceHistoryService.saveStockHistoryDaily();

    }

}

