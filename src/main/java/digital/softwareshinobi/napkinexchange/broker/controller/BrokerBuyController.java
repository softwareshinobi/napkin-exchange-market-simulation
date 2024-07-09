package digital.softwareshinobi.napkinexchange.broker.controller;

import digital.softwareshinobi.napkinexchange.broker.request.SecurityBuyRequest;
import digital.softwareshinobi.napkinexchange.broker.service.LimitOrderService;
import digital.softwareshinobi.napkinexchange.broker.service.SecurityPortfolioService;
import digital.softwareshinobi.napkinexchange.broker.types.LimitOrderType;
import digital.softwareshinobi.napkinexchange.notification.model.Notification;
import digital.softwareshinobi.napkinexchange.notification.service.NotificationService;
import digital.softwareshinobi.napkinexchange.security.model.Security;
import digital.softwareshinobi.napkinexchange.security.service.SecurityService;
import digital.softwareshinobi.napkinexchange.trader.exception.TraderBalanceException;
import digital.softwareshinobi.napkinexchange.trader.exception.TraderNotFoundException;
import digital.softwareshinobi.napkinexchange.broker.order.LimitOrder;
import digital.softwareshinobi.napkinexchange.broker.request.LimitOrderRequest;
import digital.softwareshinobi.napkinexchange.broker.response.SecurityBuyResponse;
import digital.softwareshinobi.napkinexchange.market.configuration.MarketConfiguration;
import digital.softwareshinobi.napkinexchange.notification.type.NotificationType;
import digital.softwareshinobi.napkinexchange.trader.service.TraderService;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping(value = "broker/buy")
public class BrokerBuyController {

            private final org.slf4j.Logger logger = LoggerFactory.getLogger(BrokerBuyController.class);

    private static final double DEFAULT_STOP_LOSS_TARGET_PERCENT = 0.001;

    private static final double DEFAULT_TAKE_PROFIT_TARGET_PERCENT = 0.004;

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

    public BrokerBuyController() {

        logger.debug("##");
        logger.debug("## init > Broker BUY Controller");
        logger.debug("##");

    }

    @PostMapping(value = "market")
    public SecurityBuyResponse openMarketOrder(@RequestBody SecurityBuyRequest securityBuyRequest)
            throws TraderNotFoundException, TraderBalanceException {

        logger.debug("enter > openMarketOrder");

        this.notificationService.save(new Notification(
                securityBuyRequest.getUsername(),
                NotificationType.LONG_MARKET_BUY_CREATED,
                securityBuyRequest.toString()
        ));

        logger.debug("securityBuyRequest / " + securityBuyRequest);

        logger.debug("openSimpleBuyOrder / fufilling");

        SecurityBuyResponse SecurityBuyResponse = this.securityPortfolioService.buyMarketPrice(securityBuyRequest);

        logger.debug("openSimpleBuyOrder / fulfilled");

        logger.debug("returning / " + SecurityBuyResponse);

        logger.debug("exit < openMarketOrder");

        return SecurityBuyResponse;
    }

    @PostMapping(value = "stop")
    public void openBuyStopOrder(@RequestBody LimitOrderRequest securityBuyRequest) {

        logger.debug("enter > openBuyStopOrder");

        logger.debug("securityBuyRequest / " + securityBuyRequest);

        LimitOrder newLimitOrder = new LimitOrder(
                LimitOrderType.LONG_BUY_STOP,
                this.traderService.getAccountByName(securityBuyRequest.getTrader()),
                this.securityService.getSecurityByTicker(securityBuyRequest.getTicker()),
                securityBuyRequest.getUnits(),
                securityBuyRequest.getStrike());

        logger.debug("newLimitOrder / " + newLimitOrder);

        this.limitOrderService.saveLimitOrder(newLimitOrder);

        logger.debug("exit < openBuyStopOrder");

    }

    @PostMapping(value = "smart")
    @Transactional
    public void openSmartBuyOrder(@RequestBody SecurityBuyRequest securityBuyRequest)
            throws TraderNotFoundException, TraderBalanceException {

        logger.debug("enter > openSmartBuyMarketOrder");

        this.notificationService.save(new Notification(
                securityBuyRequest.getUsername(),
                NotificationType.LONG_SMART_BUY_CREATED,
                securityBuyRequest.toString()
        ));

        ////////////////////
        logger.debug("securityBuyRequest / " + securityBuyRequest);
//
//        this.notificationService.save(
//                new Notification(
//                        limitOrder.getTrader().getUsername(),
//                        NotificationType.NEW_LONG_SMART_BUY_REQUESTED,
//                        limitOrder.toString()
//                ));

        logger.debug("buyStockRequest / filling");

        this.securityPortfolioService.buyMarketPrice(securityBuyRequest);

        logger.debug("buyStockRequest / fulfilled");
        //////////doing math ////////////

        Security security = this.securityService.getSecurityByTicker(securityBuyRequest.getTicker());

        logger.debug("stock: " + security);
        logger.debug("price / current / " + security.getPrice());
        //
        Double dynamicStopLossThreshold = security.getPrice() * (1.0 - DEFAULT_STOP_LOSS_TARGET_PERCENT);

        logger.debug("price / stop loss / " + dynamicStopLossThreshold);
        //
        Double dynamicTakeProfitThreshold = security.getPrice() * (1.0 + DEFAULT_TAKE_PROFIT_TARGET_PERCENT);

        logger.debug("price / take profit / " + dynamicTakeProfitThreshold);
        //////// creating the stop loss nd take profit orders ////////
        LimitOrder stopLossOrder = new LimitOrder(
                LimitOrderType.LONG_STOP_LOSS,
                this.traderService.getAccountByName(securityBuyRequest.getUsername()),
                this.securityService.getSecurityByTicker(securityBuyRequest.getTicker()),
                securityBuyRequest.getUnits(),
                dynamicStopLossThreshold
        );

        logger.debug("stopLossOrder / " + stopLossOrder);

        this.limitOrderService.saveLimitOrder(stopLossOrder);

        LimitOrder takeProfitOrder = new LimitOrder(
                LimitOrderType.LONG_TAKE_PROFIT,
                this.traderService.getAccountByName(securityBuyRequest.getUsername()),
                this.securityService.getSecurityByTicker(securityBuyRequest.getTicker()),
                securityBuyRequest.getUnits(),
                dynamicTakeProfitThreshold
        );

        logger.debug("takeProfitOrder / " + takeProfitOrder);

        logger.debug("7777");

        this.limitOrderService.saveLimitOrder(takeProfitOrder);

        takeProfitOrder.setPartnerID(stopLossOrder.getId());

        stopLossOrder.setPartnerID(takeProfitOrder.getId());

        logger.debug("updating the related order id");

        logger.debug("stopLossOrder / " + stopLossOrder);

        logger.debug("updating SL orders");

        Object oo = this.limitOrderService.saveLimitOrder(stopLossOrder);

        logger.debug("order / stop loss / " + oo);
////////
        logger.debug("takeProfitOrder / " + takeProfitOrder);
        logger.debug("updating TP orders");

        Object bb = this.limitOrderService.saveLimitOrder(takeProfitOrder);
        logger.debug("order / take profit / " + bb);

        logger.debug("exit < openSmartBuyMarketOrder");
    }

    @GetMapping(value = "")
    protected String root() {

        return "Broker BUY Controller";

    }

    @GetMapping(value = "/")
    protected String slash() {

        return this.root();

    }

    @GetMapping(value = "health")
    protected String health() {

        return "OK";

    }

}
