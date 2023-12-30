package digital.softwareshinobi.napkinexchange.inventory.controller;

import digital.softwareshinobi.napkinexchange.stocks.stock.service.StockService;
import digital.softwareshinobi.napkinexchange.account.exception.AccountBalanceException;
import digital.softwareshinobi.napkinexchange.account.exception.AccountNotFoundException;
import digital.softwareshinobi.napkinexchange.account.model.entity.LimitOrder;
import digital.softwareshinobi.napkinexchange.account.model.payload.BuyStockRequest;
import digital.softwareshinobi.napkinexchange.account.model.payload.LimitOrderRequest;
import digital.softwareshinobi.napkinexchange.account.model.payload.SellStockRequest;
import digital.softwareshinobi.napkinexchange.account.service.AccountService;
import digital.softwareshinobi.napkinexchange.stocks.stock.entity.Stock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping(value = "/inventory")
@CrossOrigin
public class AccountInventoryController {

    public AccountInventoryController() {

        System.out.println("AccountInventoryController AccountInventoryController ");

    }

    @Autowired
    private StockOwnedService stockOwnedService;

    @Autowired
    private LimitOrderService limitOrderService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private StockService stockService;

    @GetMapping(value = "/")
    protected String reDDDturnHealthCheckStatus() {

        return "AccountInventoryController";

    }

    @GetMapping(value = "/health")
    protected String returnHealthCheckStatus() {

        return "UP";

    }

    @RequestMapping(value = "/orders/")
    public List<LimitOrder> fetchActiveLimitOrders() {

        return limitOrderService.findLimitOrders();

    }

    @RequestMapping(value = "/orders/{username}")
    public List<LimitOrder> fetchActiveLimitOrders(@PathVariable String username) {

        return limitOrderService.findLimitOrders(accountService.getAccountByName(username));

    }

    @PostMapping(value = "/buy/market")
    public void placeAssetBuyMarketOrder(@RequestBody BuyStockRequest buyStockRequest)
            throws AccountNotFoundException, AccountBalanceException {

        System.out.println("enter > placeAssetBuyMarketOrder");

        System.out.println("buyStockRequest / " + buyStockRequest);

        stockOwnedService.fillBuyStockRequest(buyStockRequest);

        System.out.println("buyStockRequest / fulfilled");

        System.out.println("exit < placeAssetBuyMarketOrder");

    }

    @PostMapping(value = "/buy/market/auto-close")
    public void placeAssetBuyMarketOrderAutoClosing(@RequestBody BuyStockRequest buyStockRequest)
            throws AccountNotFoundException, AccountBalanceException {

        System.out.println("enter > placeAssetBuyMarketOrderAutoClosing");

        System.out.println("buyStockRequest / " + buyStockRequest);

        stockOwnedService.fillBuyStockRequest(buyStockRequest);

        System.out.println("buyStockRequest / fulfilled");

        Stock stock = stockService.getStockByTickerSymbol(buyStockRequest.getTicker());

        System.out.println("stock: " + stock);

        limitOrderService.saveLimitOrder(
                new LimitOrder(
                        LimitOrderTypes.SELL_STOP_LOSS,
                        accountService.getAccountByName(buyStockRequest.getUsername()),
                        stockService.getStockByTickerSymbol(buyStockRequest.getTicker()),
                        buyStockRequest.getSharesToBuy(),
                        (stock.getPrice() * 0.99)
                )
        );

        limitOrderService.saveLimitOrder(
                new LimitOrder(
                        LimitOrderTypes.SELL_TAKE_PROFIT,
                        accountService.getAccountByName(buyStockRequest.getUsername()),
                        stockService.getStockByTickerSymbol(buyStockRequest.getTicker()),
                        buyStockRequest.getSharesToBuy(),
                        (stock.getPrice() * 1.03)
                )
        );

        System.out.println("exit < placeAssetBuyMarketOrderAutoClosing");

    }

    @PostMapping(value = "/buy/limit/")
    public List<LimitOrder> placeAssetBuyStopSimpleLimitOrder(@RequestBody LimitOrderRequest limitOrderRequest) {

        System.out.println("enter > placeAssetBuyStopLimitOrder");

        limitOrderService.saveLimitOrder(
                new LimitOrder(
                        LimitOrderTypes.BUY_STOP,
                        accountService.getAccountByName(limitOrderRequest.getUsername()),
                        stockService.getStockByTickerSymbol(limitOrderRequest.getTicker()),
                        limitOrderRequest.getSharesToBuy(),
                        limitOrderRequest.getLimitPrice()
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
                        LimitOrderTypes.BUY_STOP,
                        accountService.getAccountByName(limitOrderRequest.getUsername()),
                        stockService.getStockByTickerSymbol(limitOrderRequest.getTicker()),
                        limitOrderRequest.getSharesToBuy(),
                        limitOrderRequest.getLimitPrice()
                )
        );

        limitOrderService.saveLimitOrder(
                new LimitOrder(
                        LimitOrderTypes.SELL_STOP_LOSS,
                        accountService.getAccountByName(limitOrderRequest.getUsername()),
                        stockService.getStockByTickerSymbol(limitOrderRequest.getTicker()),
                        limitOrderRequest.getSharesToBuy(),
                        (limitOrderRequest.getLimitPrice() * 0.99)
                )
        );

        limitOrderService.saveLimitOrder(
                new LimitOrder(
                        LimitOrderTypes.SELL_TAKE_PROFIT,
                        accountService.getAccountByName(limitOrderRequest.getUsername()),
                        stockService.getStockByTickerSymbol(limitOrderRequest.getTicker()),
                        limitOrderRequest.getSharesToBuy(),
                        (limitOrderRequest.getLimitPrice() * 1.03)
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
                        LimitOrderTypes.SELL_STOP_LOSS,
                        accountService.getAccountByName(limitOrderRequest.getUsername()),
                        stockService.getStockByTickerSymbol(limitOrderRequest.getTicker()),
                        limitOrderRequest.getSharesToBuy(),
                        limitOrderRequest.getLimitPrice())
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
                        LimitOrderTypes.SELL_TAKE_PROFIT,
                        accountService.getAccountByName(limitOrderRequest.getUsername()),
                        stockService.getStockByTickerSymbol(limitOrderRequest.getTicker()),
                        limitOrderRequest.getSharesToBuy(),
                        limitOrderRequest.getLimitPrice()
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

}
