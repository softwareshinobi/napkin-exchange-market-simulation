package digital.softwareshinobi.napkinexchange.broker.controller;

import digital.softwareshinobi.napkinexchange.broker.request.BuyStockRequest;
import digital.softwareshinobi.napkinexchange.broker.service.LimitOrderService;
import digital.softwareshinobi.napkinexchange.broker.service.SecurityPortfolioService;
import digital.softwareshinobi.napkinexchange.broker.types.LimitOrderTypes;
import digital.softwareshinobi.napkinexchange.notification.model.Notification;
import digital.softwareshinobi.napkinexchange.notification.model.NotificationType;
import digital.softwareshinobi.napkinexchange.notification.service.NotificationService;
import digital.softwareshinobi.napkinexchange.security.model.Security;
import digital.softwareshinobi.napkinexchange.security.service.SecurityService;
import digital.softwareshinobi.napkinexchange.trader.exception.AccountBalanceException;
import digital.softwareshinobi.napkinexchange.trader.exception.AccountNotFoundException;
import digital.softwareshinobi.napkinexchange.broker.order.LimitOrder;
import digital.softwareshinobi.napkinexchange.trader.service.AccountService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping(value = "broker")
public class BrokerController {

    private static final double DEFAULT_STOP_LOSS_TARGET_PERCENT = 0.01;

    private static final double DEFAULT_TAKE_PROFIT_TARGET_PERCENT = 100.02;

    public BrokerController() {

        System.out.println("##");
        System.out.println("## init > Broker Controller");
        System.out.println("##");

    }

    @Autowired
    private SecurityPortfolioService securityPortfolioService;

    @Autowired
    private LimitOrderService limitOrderService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private SecurityService securityService;

    @Autowired
    private NotificationService notificationService;

    @GetMapping(value = "/")
    protected String root() {

        return "BrokerController";

    }

    @RequestMapping(value = "/orders/")
    public List<LimitOrder> fetchActiveLimitOrders() {

        return limitOrderService.findLimitOrders();

    }

    @RequestMapping(value = "/orders/{username}")
    public List<LimitOrder> fetchActiveLimitOrders(@PathVariable String username) {

        return limitOrderService.findLimitOrders(accountService.getAccountByName(username));

    }

    @PostMapping(value = "/buy/market/smart")
    public void openSmartBuyMarketOrder(@RequestBody BuyStockRequest buyStockRequest)
            throws AccountNotFoundException, AccountBalanceException {

        System.out.println("enter > openSmartBuyMarketOrder");

        this.notificationService.save(
                new Notification(
                        buyStockRequest.getUsername(),
                        NotificationType.LONG_SMART_BUY_CREATED,
                        buyStockRequest.toString()
                ));

        ////////////////////
        System.out.println("buyStockRequest / " + buyStockRequest);
//
//        this.notificationService.save(
//                new Notification(
//                        limitOrder.getAccount().getUsername(),
//                        NotificationType.NEW_LONG_SMART_BUY_REQUESTED,
//                        limitOrder.toString()
//                ));

        securityPortfolioService.fillBuyMarketStockRequest(buyStockRequest);

        System.out.println("buyStockRequest / fulfilled");

        //////////doing math ////////////
        Security stock = securityService.getSecurityBySymbol(buyStockRequest.getTicker());

        System.out.println("stock: " + stock);

        System.out.println("price / current / " + stock.getPrice());

        //
        Double dynamicStopLossThreshold = stock.getPrice() * (1.0 - DEFAULT_STOP_LOSS_TARGET_PERCENT);

        System.out.println("price / stop loss / " + dynamicStopLossThreshold);

        //
        Double dynamicTakeProfitThreshold = stock.getPrice() * (1.0 + DEFAULT_TAKE_PROFIT_TARGET_PERCENT);

        System.out.println("price / take profit / " + dynamicTakeProfitThreshold);

        //////// creating the stop loss nd take profit orders ////////
        LimitOrder stopLossOrder = new LimitOrder(
                LimitOrderTypes.LONG_STOP_LOSS,
                accountService.getAccountByName(buyStockRequest.getUsername()),
                securityService.getSecurityBySymbol(buyStockRequest.getTicker()),
                buyStockRequest.getSharesToBuy(),
                dynamicStopLossThreshold
        );

        LimitOrder takeProfitOrder = new LimitOrder(
                LimitOrderTypes.LONG_TAKE_PROFIT,
                accountService.getAccountByName(buyStockRequest.getUsername()),
                securityService.getSecurityBySymbol(buyStockRequest.getTicker()),
                buyStockRequest.getSharesToBuy(),
                dynamicTakeProfitThreshold
        );

        limitOrderService.saveLimitOrder(stopLossOrder);

        limitOrderService.saveLimitOrder(takeProfitOrder);
        
        takeProfitOrder.setRelatedOrderId(stopLossOrder.getId());

        stopLossOrder.setRelatedOrderId(takeProfitOrder.getId());

        limitOrderService.saveLimitOrder(stopLossOrder);

        limitOrderService.saveLimitOrder(takeProfitOrder);
////////
        System.out.println("order / stop loss / " + stopLossOrder);

        System.out.println("order / take profit / " + takeProfitOrder);

        System.out.println("exit < openSmartBuyMarketOrder");

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
////        stringBuffer.append(", stock=").append(stopLossOrder.getStock());
////        stringBuffer.append(", sharesToBuy=").append(stopLossOrder.getSharesToBuy());
////        stringBuffer.append(", strikePrice=").append(stopLossOrder.getStrikePrice());
////        stringBuffer.append(", relatedOrder=").append(stopLossOrder.getRelatedOrderId());
////
////        //"Created future or limit order created / " + limitOrder.toString()
/////////////////////////////////
////        //    System.out.println("takeProfitOrder / " + takeProfitOrder);
////        notificationService.save(
////                new Notification(
////                        stopLossOrder.getAccount().getUsername(),
////                        NotificationType.LONG_SMART_BUY_FULFILLED,
////                        "long smart buy activities completed"
////                ));

    @PostMapping(value = "/buy/market")
    public void placeAssetBuyMarketOrder(@RequestBody BuyStockRequest buyStockRequest)
            throws AccountNotFoundException, AccountBalanceException {

        System.out.println("enter > placeAssetBuyMarketOrder");

        System.out.println("buyStockRequest / " + buyStockRequest);

        stockOwnedService.fillStandardBuyStockRequest(buyStockRequest);

        System.out.println("buyStockRequest / fulfilled");

        System.out.println("exit < placeAssetBuyMarketOrder");

    }



    @PostMapping(value = "/buy/limit/")
    public List<LimitOrder> placeAssetBuyStopSimpleLimitOrder(@RequestBody LimitOrderRequest limitOrderRequest) {

        System.out.println("enter > placeAssetBuyStopLimitOrder");

        limitOrderService.saveLimitOrder(
                new LimitOrder(
                        LimitOrderTypes.LONG_BUY_STOP,
                        accountService.getAccountByName(limitOrderRequest.getUsername()),
                        stockService.getSecurityBySymbol(limitOrderRequest.getTicker()),
                        limitOrderRequest.getSharesToBuy(),
                        limitOrderRequest.getStrikePrice()
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
                        LimitOrderTypes.LONG_BUY_STOP,
                        accountService.getAccountByName(limitOrderRequest.getUsername()),
                        stockService.getSecurityBySymbol(limitOrderRequest.getTicker()),
                        limitOrderRequest.getSharesToBuy(),
                        limitOrderRequest.getStrikePrice()
                )
        );

        limitOrderService.saveLimitOrder(
                new LimitOrder(
                        LimitOrderTypes.LONG_STOP_LOSS,
                        accountService.getAccountByName(limitOrderRequest.getUsername()),
                        stockService.getSecurityBySymbol(limitOrderRequest.getTicker()),
                        limitOrderRequest.getSharesToBuy(),
                        (limitOrderRequest.getStrikePrice() * 0.99)
                )
        );

        limitOrderService.saveLimitOrder(
                new LimitOrder(
                        LimitOrderTypes.LONG_TAKE_PROFIT,
                        accountService.getAccountByName(limitOrderRequest.getUsername()),
                        stockService.getSecurityBySymbol(limitOrderRequest.getTicker()),
                        limitOrderRequest.getSharesToBuy(),
                        (limitOrderRequest.getStrikePrice() * 1.03)
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
                        LimitOrderTypes.LONG_STOP_LOSS,
                        accountService.getAccountByName(limitOrderRequest.getUsername()),
                        stockService.getSecurityBySymbol(limitOrderRequest.getTicker()),
                        limitOrderRequest.getSharesToBuy(),
                        limitOrderRequest.getStrikePrice())
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
                        LimitOrderTypes.LONG_TAKE_PROFIT,
                        accountService.getAccountByName(limitOrderRequest.getUsername()),
                        stockService.getSecurityBySymbol(limitOrderRequest.getTicker()),
                        limitOrderRequest.getSharesToBuy(),
                        limitOrderRequest.getStrikePrice()
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
