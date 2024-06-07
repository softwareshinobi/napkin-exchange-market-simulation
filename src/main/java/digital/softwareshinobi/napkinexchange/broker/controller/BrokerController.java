package digital.softwareshinobi.napkinexchange.broker.controller;

import digital.softwareshinobi.napkinexchange.broker.request.SecurityBuyRequest;
import digital.softwareshinobi.napkinexchange.broker.service.LimitOrderService;
import digital.softwareshinobi.napkinexchange.broker.service.SecurityPortfolioService;
import digital.softwareshinobi.napkinexchange.broker.types.LimitOrderType;
import digital.softwareshinobi.napkinexchange.notification.model.Notification;
import digital.softwareshinobi.napkinexchange.notification.model.NotificationType;
import digital.softwareshinobi.napkinexchange.notification.service.NotificationService;
import digital.softwareshinobi.napkinexchange.security.model.Security;
import digital.softwareshinobi.napkinexchange.security.service.SecurityService;
import digital.softwareshinobi.napkinexchange.trader.exception.TraderBalanceException;
import digital.softwareshinobi.napkinexchange.trader.exception.TraderNotFoundException;
import digital.softwareshinobi.napkinexchange.broker.order.LimitOrder;
import digital.softwareshinobi.napkinexchange.trader.service.TraderService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping(value = "broker")
public class BrokerController {

    private static final double DEFAULT_STOP_LOSS_TARGET_PERCENT = 0.01;

    private static final double DEFAULT_TAKE_PROFIT_TARGET_PERCENT = 0.025;

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

    public BrokerController() {

        System.out.println("##");
        System.out.println("## init > Broker Controller");
        System.out.println("##");

    }

    @RequestMapping(value = "/orders/")
    public List<LimitOrder> fetchActiveLimitOrders() {

        return this.limitOrderService.findLimitOrders();

    }

    @RequestMapping(value = "/orders/{username}")
    public List<LimitOrder> fetchActiveLimitOrders(@PathVariable String username) {

        return this.limitOrderService.findLimitOrders(traderService.getAccountByName(username));

    }

    @GetMapping(value = "/")
    protected String root() {

        return "BrokerController";

    }

    @GetMapping(value = "health")
    protected String health() {

        return "OK";

    }
    /*
        /// build notification
////        StringBuilder stringBuffer = new StringBuilder();
////        stringBuffer.append("[SMART] created stop loss. ");//.append(stopLossOrder);
////        stringBuffer.append("id=").append(stopLossOrder.getId());
////        stringBuffer.append(", type=").append(stopLossOrder.getType());
////        stringBuffer.append(", stock=").append(stopLossOrder.getSecurity());
////        stringBuffer.append(", sharesToBuy=").append(stopLossOrder.getUnits());
////        stringBuffer.append(", strikePrice=").append(stopLossOrder.getStrike());
////        stringBuffer.append(", relatedOrder=").append(stopLossOrder.getRelatedOrderId());
////
////        //"Created future or limit order created / " + limitOrder.toString()
/////////////////////////////////
////        //    System.out.println("takeProfitOrder / " + takeProfitOrder);
////        notificationService.save(
////                new Notification(
////                        stopLossOrder.getTrader().getUsername(),
////                        NotificationType.LONG_SMART_BUY_FULFILLED,
////                        "long smart buy activities completed"
////                ));


    @PostMapping(value = "/buy/limit/")
    public List<LimitOrder> placeAssetBuyStopSimpleLimitOrder(@RequestBody LimitOrderRequest limitOrderRequest) {

        System.out.println("enter > placeAssetBuyStopLimitOrder");

        limitOrderService.saveLimitOrder(
                new LimitOrder(
                        LimitOrderType.LONG_BUY_STOP,
                        accountService.getAccountByName(limitOrderRequest.getUsername()),
                        stockService.getSecurityBySymbol(limitOrderRequest.getTicker()),
                        limitOrderRequest.getUnits(),
                        limitOrderRequest.getStrike()
                )
        );

        List<LimitOrder> userLimitOrderList = limitOrderService.findLimitOrders(
                accountService.getAccountByName(
                        limitOrderRequest.getUsername()
                )
        );

        System.out.println("exit < placeAssetBuyStopLimitOrder");

        return userLimitOrderList;

    }

    @PostMapping(value = "/buy/limit/auto-close")
    public List<LimitOrder> placeAssetBuyStopClosingLimitOrder(@RequestBody LimitOrderRequest limitOrderRequest) {

        System.out.println("enter > placeAssetBuyStopLimitOrder");

        limitOrderService.saveLimitOrder(
                new LimitOrder(
                        LimitOrderType.LONG_BUY_STOP,
                        accountService.getAccountByName(limitOrderRequest.getUsername()),
                        stockService.getSecurityBySymbol(limitOrderRequest.getTicker()),
                        limitOrderRequest.getUnits(),
                        limitOrderRequest.getStrike()
                )
        );

        limitOrderService.saveLimitOrder(
                new LimitOrder(
                        LimitOrderType.LONG_STOP_LOSS,
                        accountService.getAccountByName(limitOrderRequest.getUsername()),
                        stockService.getSecurityBySymbol(limitOrderRequest.getTicker()),
                        limitOrderRequest.getUnits(),
                        (limitOrderRequest.getStrike() * 0.99)
                )
        );

        limitOrderService.saveLimitOrder(
                new LimitOrder(
                        LimitOrderType.LONG_TAKE_PROFIT,
                        accountService.getAccountByName(limitOrderRequest.getUsername()),
                        stockService.getSecurityBySymbol(limitOrderRequest.getTicker()),
                        limitOrderRequest.getUnits(),
                        (limitOrderRequest.getStrike() * 1.03)
                )
        );

        List<LimitOrder> userLimitOrderList = limitOrderService.findLimitOrders(
                accountService.getAccountByName(
                        limitOrderRequest.getUsername()
                )
        );

        System.out.println("exit < placeAssetBuyStopLimitOrder");

        return userLimitOrderList;

    }

    @PostMapping(value = "/sell/market")
    protected void sellAssetUnitsMarketPrice(@RequestBody SellStockRequest sellStockRequest)
            throws AccountNotFoundException, AccountBalanceException, AccountInventoryException {

        System.out.println("enter > sellAssetUnitsMarketPrice");

        stockOwnedService.sellStockMarketPrice(sellStockRequest);

        System.out.println("exit < sellAssetUnitsMarketPrice");

    }

    @PostMapping(value = "/sell/stop-loss")
    protected List<LimitOrder> sellAssetUnitsStopLoss(@RequestBody LimitOrderRequest limitOrderRequest)
            throws AccountNotFoundException, AccountBalanceException, AccountInventoryException {

        System.out.println("enter > sellAssetUnitsStopLoss");

        limitOrderService.saveLimitOrder(
                new LimitOrder(
                        LimitOrderType.LONG_STOP_LOSS,
                        accountService.getAccountByName(limitOrderRequest.getUsername()),
                        stockService.getSecurityBySymbol(limitOrderRequest.getTicker()),
                        limitOrderRequest.getUnits(),
                        limitOrderRequest.getStrike())
        );

        List<LimitOrder> userLimitOrderList
                = limitOrderService.findLimitOrders(
                        accountService.getAccountByName(
                                limitOrderRequest.getUsername()));

        System.out.println("exit < sellAssetUnitsStopLoss");

        return userLimitOrderList;

    }

    @PostMapping(value = "/sell/take-profit")
    protected List<LimitOrder> sellAssetUnitsTakeProfit(@RequestBody LimitOrderRequest limitOrderRequest)
            throws AccountNotFoundException, AccountBalanceException, AccountInventoryException {

        System.out.println("enter > sellAssetUnitsTakeProfit");

        limitOrderService.saveLimitOrder(
                new LimitOrder(
                        LimitOrderType.LONG_TAKE_PROFIT,
                        accountService.getAccountByName(limitOrderRequest.getUsername()),
                        stockService.getSecurityBySymbol(limitOrderRequest.getTicker()),
                        limitOrderRequest.getUnits(),
                        limitOrderRequest.getStrike()
                )
        );

        List<LimitOrder> userLimitOrderList
                = limitOrderService.findLimitOrders(
                        accountService.getAccountByName(
                                limitOrderRequest.getUsername()
                        )
                );

        System.out.println("exit < sellAssetUnitsTakeProfit");

        return userLimitOrderList;

    }

     */
}
