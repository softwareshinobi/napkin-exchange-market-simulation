package digital.softwareshinobi.napkinexchange.market.scheduler;

import digital.softwareshinobi.napkinexchange.broker.service.LimitOrderService;
import digital.softwareshinobi.napkinexchange.market.constants.MarketIntervals;
import digital.softwareshinobi.napkinexchange.market.service.MarketActivityService;
import digital.softwareshinobi.napkinexchange.security.service.StockPriceHistoryService;
import digital.softwareshinobi.napkinexchange.trader.service.AccountHistoryService;
import lombok.AllArgsConstructor;
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
    private final MarketActivityService marketActivityService;

    @Autowired
    private final LimitOrderService limitOrderService;

    @Autowired
    private final AccountHistoryService accountHistoryService;

    @Autowired
    private final StockPriceHistoryService stockPriceHistoryService;

    @Scheduled(fixedRate = MarketIntervals.ONE_SECOND)
    @SuppressWarnings("unused")
    public void dailyMarketActivity() {

        var now = marketActivityService.tickMarket();

        logger.info("market time / " + now);

        marketActivityService.executeMarketTickActivities();

        limitOrderService.processLimitOrders();

        accountHistoryService.saveDailyAccountHistory();

        stockPriceHistoryService.saveStockHistoryDaily();

    }

}
