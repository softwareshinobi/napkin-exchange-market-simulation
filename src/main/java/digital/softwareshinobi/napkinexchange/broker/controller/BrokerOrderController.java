package digital.softwareshinobi.napkinexchange.broker.controller;

import digital.softwareshinobi.napkinexchange.broker.service.LimitOrderService;
import digital.softwareshinobi.napkinexchange.broker.service.SecurityPortfolioService;
import digital.softwareshinobi.napkinexchange.security.service.SecurityService;
import digital.softwareshinobi.napkinexchange.broker.order.LimitOrder;
import digital.softwareshinobi.napkinexchange.notification.service.NotificationService;
import digital.softwareshinobi.napkinexchange.trader.service.TraderService;
import java.util.List;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping(value = "broker/orders")
public class BrokerOrderController {

                private final org.slf4j.Logger logger = LoggerFactory.getLogger(BrokerOrderController.class);

    @Autowired
    private SecurityPortfolioService securityPortfolioService;

    @Autowired
    private LimitOrderService limitOrderService;

    @Autowired
    private TraderService traderService;

    @Autowired
    private SecurityService securityService;

    @Autowired
    private NotificationService notificationService;

    public BrokerOrderController() {

        logger.debug("##");
        logger.debug("## init > Broker ORDER Controller");
        logger.debug("##");

    }

    @RequestMapping(value = "")
    public List<LimitOrder> fetchActiveLimitOrders() {

        return this.limitOrderService.findLimitOrders();

    }

    @RequestMapping(value = "{username}")
    public List<LimitOrder> fetchActiveLimitOrders(@PathVariable String username) {

        return this.limitOrderService.findLimitOrders(this.traderService.getAccountByName(username));

    }

    @GetMapping(value = "health")
    protected String health() {

        return "OK";

    }

}
