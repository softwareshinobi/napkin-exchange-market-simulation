package digital.softwareshinobi.napkinexchange.market.scheduled;

import lombok.AllArgsConstructor;
import digital.softwareshinobi.napkinexchange.indexfund.service.IndexFundService;
import digital.softwareshinobi.napkinexchange.market.constants.MarketIntervals;
import digital.softwareshinobi.napkinexchange.stocks.stock.service.StockPriceHistoryService;
import digital.softwareshinobi.napkinexchange.account.service.AccountHistoryService;
import digital.softwareshinobi.napkinexchange.inventory.controller.LimitOrderService;
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

    @Autowired
    private final IndexFundService indexFundService;

    @Scheduled(fixedRate = MarketIntervals.ONE_SECOND)
    @SuppressWarnings("unused")
    public void dailyMarketActivity() {

        var now = handleMarketActivity.tickMarket();

        logger.info("market time / " + now);

        handleMarketActivity.dailyMarketActivity();

        limitOrderService.processAllLimitOrders();

        accountHistoryService.saveDailyAccountHistory();

        stockPriceHistoryService.saveStockHistoryDaily();

        indexFundService.updatePriceForAllFundsDaily();

    }

}

