package digital.softwareshinobi.napkinexchange.broker.controller;

import digital.softwareshinobi.napkinexchange.broker.request.BuyStockRequest;
import digital.softwareshinobi.napkinexchange.broker.service.LimitOrderService;
import digital.softwareshinobi.napkinexchange.broker.service.StockOwnedService;
import digital.softwareshinobi.napkinexchange.broker.types.LimitOrderTypes;
import digital.softwareshinobi.napkinexchange.notification.model.Notification;
import digital.softwareshinobi.napkinexchange.notification.service.NotificationService;
import digital.softwareshinobi.napkinexchange.notification.model.NotificationType;
import digital.softwareshinobi.napkinexchange.security.entity.Stock;
import digital.softwareshinobi.napkinexchange.security.service.StockService;
import digital.softwareshinobi.napkinexchange.trader.exception.AccountBalanceException;
import digital.softwareshinobi.napkinexchange.trader.exception.AccountNotFoundException;
import digital.softwareshinobi.napkinexchange.trader.model.LimitOrder;
import digital.softwareshinobi.napkinexchange.trader.service.AccountService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping(value = "broker")
public class BrokerController {

    public BrokerController() {

        System.out.println("init > BrokerController");

    }

    @Autowired
    private StockOwnedService stockOwnedService;

    @Autowired
    private LimitOrderService limitOrderService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private StockService stockService;

    @Autowired
    private NotificationService notificationService;

    @GetMapping(value = "/")
    protected String root() {

        return "BrokerController";

    }

    @GetMapping(value = "health")
    protected String health() {

        return "OK";

    }

    @PostMapping(value = "/buy/market/smart")
    public void openSmartBuyMarketOrder(@RequestBody BuyStockRequest buyStockRequest)
            throws AccountNotFoundException, AccountBalanceException {

        System.out.println("enter > openSmartBuyMarketOrder");

        System.out.println("buyStockRequest / " + buyStockRequest);

        stockOwnedService.fillBuyStockRequest(buyStockRequest);

        System.out.println("buyStockRequest / fulfilled");

        Stock stock = stockService.getStockByTickerSymbol(buyStockRequest.getTicker());

        System.out.println("stock: " + stock);

        LimitOrder stopLossOrder = new LimitOrder(
                LimitOrderTypes.LONG_STOP_LOSS,
                accountService.getAccountByName(buyStockRequest.getUsername()),
                stockService.getStockByTickerSymbol(buyStockRequest.getTicker()),
                buyStockRequest.getSharesToBuy(),
                //todo parameterize this percentage
                (stock.getPrice() * 0.999)
        );

        LimitOrder takeProfitOrder = new LimitOrder(
                LimitOrderTypes.LONG_TAKE_PROFIT,
                accountService.getAccountByName(buyStockRequest.getUsername()),
                stockService.getStockByTickerSymbol(buyStockRequest.getTicker()),
                buyStockRequest.getSharesToBuy(),
                //todo parameterize this percentage
                (stock.getPrice() * 1.3)
        );

        limitOrderService.saveLimitOrder(stopLossOrder);

        limitOrderService.saveLimitOrder(takeProfitOrder);

        takeProfitOrder.setRelatedOrderId(stopLossOrder.getId());

        stopLossOrder.setRelatedOrderId(takeProfitOrder.getId());

        //
        System.out.println("  stopLossOrder / " + stopLossOrder);
        System.out.println("takeProfitOrder / " + takeProfitOrder);
        //       System.exit(10);

        /// build notification
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("[SMART] created stop loss. ");//.append(stopLossOrder);

        //    StringBuilder sb = new StringBuilder();
        stringBuffer.append("id=").append(stopLossOrder.getId());
        // sb.append(", account=").append(account);
        stringBuffer.append(", type=").append(stopLossOrder.getType());
        stringBuffer.append(", stock=").append(stopLossOrder.getStock());
        stringBuffer.append(", sharesToBuy=").append(stopLossOrder.getSharesToBuy());
        stringBuffer.append(", strikePrice=").append(stopLossOrder.getStrikePrice());
        stringBuffer.append(", relatedOrder=").append(stopLossOrder.getRelatedOrderId());

        //"Created future or limit order created / " + limitOrder.toString()
        notificationService.save(
                new Notification(
                        stopLossOrder.getAccount().getUsername(),
                        NotificationType.LONG_STOP_LOSS,
                        stringBuffer.toString()
                ));

/////////////////////////////
        System.out.println("takeProfitOrder / " + takeProfitOrder);

        System.out.println("exit < openSmartBuyMarketOrder");

    }

    @RequestMapping(value = "/orders/")
    public List<LimitOrder> fetchActiveLimitOrders() {

        return limitOrderService.findLimitOrders();

    }

    @RequestMapping(value = "/orders/{username}")
    public List<LimitOrder> fetchActiveLimitOrders(@PathVariable String username) {

        return limitOrderService.findLimitOrders(accountService.getAccountByName(username));

    }
    /*


    @PostMapping(value = "/buy/market")
    public void placeAssetBuyMarketOrder(@RequestBody BuyStockRequest buyStockRequest)
            throws AccountNotFoundException, AccountBalanceException {

        System.out.println("enter > placeAssetBuyMarketOrder");

        System.out.println("buyStockRequest / " + buyStockRequest);

        stockOwnedService.fillBuyStockRequest(buyStockRequest);

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
                        stockService.getStockByTickerSymbol(limitOrderRequest.getTicker()),
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
                        stockService.getStockByTickerSymbol(limitOrderRequest.getTicker()),
                        limitOrderRequest.getSharesToBuy(),
                        limitOrderRequest.getStrikePrice()
                )
        );

        limitOrderService.saveLimitOrder(
                new LimitOrder(
                        LimitOrderTypes.LONG_STOP_LOSS,
                        accountService.getAccountByName(limitOrderRequest.getUsername()),
                        stockService.getStockByTickerSymbol(limitOrderRequest.getTicker()),
                        limitOrderRequest.getSharesToBuy(),
                        (limitOrderRequest.getStrikePrice() * 0.99)
                )
        );

        limitOrderService.saveLimitOrder(
                new LimitOrder(
                        LimitOrderTypes.LONG_TAKE_PROFIT,
                        accountService.getAccountByName(limitOrderRequest.getUsername()),
                        stockService.getStockByTickerSymbol(limitOrderRequest.getTicker()),
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
                        stockService.getStockByTickerSymbol(limitOrderRequest.getTicker()),
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
                        stockService.getStockByTickerSymbol(limitOrderRequest.getTicker()),
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
