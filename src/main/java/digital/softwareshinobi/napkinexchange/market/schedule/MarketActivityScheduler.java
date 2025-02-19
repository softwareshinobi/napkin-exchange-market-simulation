package digital.softwareshinobi.napkinexchange.market.schedule;

import digital.softwareshinobi.napkinexchange.broker.service.LimitOrderService;
import digital.softwareshinobi.napkinexchange.market.constants.MarketIntervals;
import digital.softwareshinobi.napkinexchange.market.service.MarketActivityService;
import digital.softwareshinobi.napkinexchange.security.service.SecurityHistoryService;
import digital.softwareshinobi.napkinexchange.trader.service.TraderHistoryService;
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
    private final TraderHistoryService traderHistoryService;

    @Autowired
    private final SecurityHistoryService securityHistoryService;

    @SuppressWarnings("unused")
    @Scheduled(fixedRate = MarketIntervals.T)
    public void dailyMarketActivity() {

        var currentMarketTime = this.marketActivityService.tick();

        logger.info("tick / market time / " + currentMarketTime);

        this.limitOrderService.processLimitOrders();

        this.securityHistoryService.updateSecurityHistory();

        this.traderHistoryService.updateTraderAccountHistory();

        currentMarketTime = null;

    }

}
