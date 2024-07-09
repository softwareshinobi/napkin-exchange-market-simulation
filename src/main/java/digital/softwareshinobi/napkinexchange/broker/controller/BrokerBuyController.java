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
import digital.softwareshinobi.napkinexchange.notification.type.NotificationType;
import digital.softwareshinobi.napkinexchange.trader.service.TraderService;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping(value = "broker/buy")
public class BrokerBuyController {

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

        System.out.println("##");
        System.out.println("## init > Broker BUY Controller");
        System.out.println("##");

    }

    @PostMapping(value = "market")
    public SecurityBuyResponse openMarketOrder(@RequestBody SecurityBuyRequest securityBuyRequest)
            throws TraderNotFoundException, TraderBalanceException {

        System.out.println("enter > openMarketOrder");

        this.notificationService.save(new Notification(
                securityBuyRequest.getUsername(),
                NotificationType.LONG_MARKET_BUY_CREATED,
                securityBuyRequest.toString()
        ));

        System.out.println("securityBuyRequest / " + securityBuyRequest);

        System.out.println("openSimpleBuyOrder / fufilling");

        SecurityBuyResponse SecurityBuyResponse = this.securityPortfolioService.buyMarketPrice(securityBuyRequest);

        System.out.println("openSimpleBuyOrder / fulfilled");

        System.out.println("returning / " + SecurityBuyResponse);

        System.out.println("exit < openMarketOrder");

        return SecurityBuyResponse;
    }

    @PostMapping(value = "stop")
    public void openBuyStopOrder(@RequestBody LimitOrderRequest securityBuyRequest) {

        System.out.println("enter > openBuyStopOrder");

        System.out.println("securityBuyRequest / " + securityBuyRequest);

        LimitOrder newLimitOrder = new LimitOrder(
                LimitOrderType.LONG_BUY_STOP,
                this.traderService.getAccountByName(securityBuyRequest.getTrader()),
                this.securityService.getSecurityBySymbol(securityBuyRequest.getTicker()),
                securityBuyRequest.getUnits(),
                securityBuyRequest.getStrike());

        System.out.println("newLimitOrder / " + newLimitOrder);

        this.limitOrderService.saveLimitOrder(newLimitOrder);

        System.out.println("exit < openBuyStopOrder");

    }

    @PostMapping(value = "smart")
    @Transactional
    public void openSmartBuyOrder(@RequestBody SecurityBuyRequest securityBuyRequest)
            throws TraderNotFoundException, TraderBalanceException {

        System.out.println("enter > openSmartBuyMarketOrder");

        this.notificationService.save(new Notification(
                securityBuyRequest.getUsername(),
                NotificationType.LONG_SMART_BUY_CREATED,
                securityBuyRequest.toString()
        ));

        ////////////////////
        System.out.println("securityBuyRequest / " + securityBuyRequest);
//
//        this.notificationService.save(
//                new Notification(
//                        limitOrder.getTrader().getUsername(),
//                        NotificationType.NEW_LONG_SMART_BUY_REQUESTED,
//                        limitOrder.toString()
//                ));

        System.out.println("buyStockRequest / filling");

        this.securityPortfolioService.buyMarketPrice(securityBuyRequest);

        System.out.println("buyStockRequest / fulfilled");
        //////////doing math ////////////

        Security security = this.securityService.getSecurityBySymbol(securityBuyRequest.getTicker());

        System.out.println("stock: " + security);
        System.out.println("price / current / " + security.getPrice());
        //
        Double dynamicStopLossThreshold = security.getPrice() * (1.0 - DEFAULT_STOP_LOSS_TARGET_PERCENT);

        System.out.println("price / stop loss / " + dynamicStopLossThreshold);
        //
        Double dynamicTakeProfitThreshold = security.getPrice() * (1.0 + DEFAULT_TAKE_PROFIT_TARGET_PERCENT);

        System.out.println("price / take profit / " + dynamicTakeProfitThreshold);
        //////// creating the stop loss nd take profit orders ////////
        LimitOrder stopLossOrder = new LimitOrder(
                LimitOrderType.LONG_STOP_LOSS,
                this.traderService.getAccountByName(securityBuyRequest.getUsername()),
                this.securityService.getSecurityBySymbol(securityBuyRequest.getTicker()),
                securityBuyRequest.getUnits(),
                dynamicStopLossThreshold
        );

        System.out.println("stopLossOrder / " + stopLossOrder);

        this.limitOrderService.saveLimitOrder(stopLossOrder);

        LimitOrder takeProfitOrder = new LimitOrder(
                LimitOrderType.LONG_TAKE_PROFIT,
                this.traderService.getAccountByName(securityBuyRequest.getUsername()),
                this.securityService.getSecurityBySymbol(securityBuyRequest.getTicker()),
                securityBuyRequest.getUnits(),
                dynamicTakeProfitThreshold
        );

        System.out.println("takeProfitOrder / " + takeProfitOrder);

        System.out.println("7777");

        this.limitOrderService.saveLimitOrder(takeProfitOrder);

        takeProfitOrder.setPartnerID(stopLossOrder.getId());

        stopLossOrder.setPartnerID(takeProfitOrder.getId());

        System.out.println("updating the related order id");

        System.out.println("stopLossOrder / " + stopLossOrder);

        System.out.println("updating SL orders");

        Object oo = this.limitOrderService.saveLimitOrder(stopLossOrder);

        System.out.println("order / stop loss / " + oo);
////////
        System.out.println("takeProfitOrder / " + takeProfitOrder);
        System.out.println("updating TP orders");

        Object bb = this.limitOrderService.saveLimitOrder(takeProfitOrder);
        System.out.println("order / take profit / " + bb);

        System.out.println("exit < openSmartBuyMarketOrder");
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
