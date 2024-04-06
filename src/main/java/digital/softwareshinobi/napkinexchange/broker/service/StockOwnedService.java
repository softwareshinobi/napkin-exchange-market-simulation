package digital.softwareshinobi.napkinexchange.broker.service;

import digital.softwareshinobi.napkinexchange.broker.exception.AccountInventoryException;
import digital.softwareshinobi.napkinexchange.broker.request.BuyStockRequest;
import digital.softwareshinobi.napkinexchange.broker.request.SellStockRequest;
import digital.softwareshinobi.napkinexchange.notification.model.Notification;
import digital.softwareshinobi.napkinexchange.notification.service.NotificationService;
import digital.softwareshinobi.napkinexchange.notification.model.NotificationType;
import digital.softwareshinobi.napkinexchange.security.entity.Stock;
import digital.softwareshinobi.napkinexchange.security.service.StockService;
import digital.softwareshinobi.napkinexchange.trader.exception.AccountBalanceException;
import digital.softwareshinobi.napkinexchange.trader.model.Account;
import digital.softwareshinobi.napkinexchange.trader.model.StockOwned;
import digital.softwareshinobi.napkinexchange.trader.repository.StockOwnedRepository;
import digital.softwareshinobi.napkinexchange.trader.service.AccountService;
import digital.softwareshinobi.napkinexchange.trader.utility.ValidateStockTransaction;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class StockOwnedService {

    @Autowired
    private final StockOwnedRepository stockOwnedRepository;

    @Autowired
    private final AccountService accountService;

    @Autowired
    private final StockService stockService;

    @Autowired
    private final NotificationService notificationService;

    public void fillBuyStockRequest(BuyStockRequest buyStockRequest) {

        System.out.println("enter > placeAssetBuyMarketOrder");

        System.out.println("buyStockRequest / " + buyStockRequest);

        Account account = accountService.getAccountByName(buyStockRequest.getUsername());

        Stock stock = stockService.getStockByTickerSymbol(buyStockRequest.getTicker());

        StockOwned stockOwned = findStockOwned(account, stock);

        if (!ValidateStockTransaction.doesAccountHaveEnoughMoney(account, buyStockRequest, this.stockService)) {

            throw new AccountBalanceException("Account does not have funds for this purchase");

        }

        notificationService.save(
                new Notification(
                        buyStockRequest.getUsername(),
                        NotificationType.LONGBUY,
                        "BOUGHT NEW STOCK / " + buyStockRequest.toString()
                ));

        if (stockOwned != null) {

            //subtract transaction value from account balance
            accountService.updateBalanceAndSave(account, -1 * (buyStockRequest.getSharesToBuy() * stock.getPrice()));

            stockOwned.updateCostBasisAndAmountOwned(buyStockRequest.getSharesToBuy(), stock.getPrice());

            stockOwnedRepository.save(stockOwned);

            return;

        }

        accountService.updateBalanceAndSave(account, -1 * (buyStockRequest.getSharesToBuy() * stock.getPrice()));

        saveNewStockOwned(buyStockRequest, account, stock.getPrice());

    }

    public void saveNewStockOwned(BuyStockRequest buyStockRequest, Account account, double stockPrice) {

        stockOwnedRepository.save(new StockOwned(
                account,
                buyStockRequest.getTicker(),
                buyStockRequest.getSharesToBuy(),
                stockPrice
        ));

//        notificationService.save(
//                new Notification(
//                        buyStockRequest.getUsername(),
//                        NotificationType.LONGBUY,
//                        "BOUGHT NEW STOCK / " + buyStockRequest.toString()
//                ));
    }

    public void sellStockMarketPrice(SellStockRequest sellStockRequest) {

        System.out.println("sellStocksellStocksellStock");

        Account account = accountService.getAccountByName(sellStockRequest.getUsername());

        System.out.println("1");

        if (!ValidateStockTransaction.doesAccountHaveEnoughStocks(account, sellStockRequest)) {

            throw new AccountInventoryException("Account does not own enough stocks");

        }

        System.out.println("2.0");

        Stock stock = stockService.getStockByTickerSymbol(sellStockRequest.getSecurity());

        System.out.println("2");

        StockOwned stockOwned = findStockOwned(account, stock);

        System.out.println("3");

        account.updateTotalProfits(
                stockOwned.getCostBasis(),
                sellStockRequest.getUnits(),
                stock.getPrice());

        System.out.println("4");

        accountService.updateBalanceAndSave(account, stock.getPrice() * sellStockRequest.getUnits());

        notificationService.save(
                new Notification(
                        sellStockRequest.getUsername(),
                        NotificationType.LONGBUY,
                        "SOLD NEW STOCK / " + sellStockRequest.toString()
                ));

        System.out.println("5");

        if (sellStockRequest.getUnits() - stockOwned.getAmountOwned() == 0) {

            System.out.println("6");

//            clearAndDeleteStockOwned(stockOwned);
        } else {

            System.out.println("7");

            stockOwned.setAmountOwned(stockOwned.getAmountOwned() - sellStockRequest.getUnits());

            stockOwnedRepository.save(stockOwned);

        }

    }

    public StockOwned findStockOwned(Account account, Stock stock) {

        return stockOwnedRepository.findAll().stream()
                .filter(stockOwned -> stockOwned.getTicker().equalsIgnoreCase(stock.getTicker()))
                .filter(stockOwned -> stockOwned.getAccount().getUsername().equals(account.getUsername()))
                .findFirst()
                .orElse(null);

    }

//    public void clearAndDeleteStockOwned(StockOwned stockOwned) {
//
//        stockOwnedRepository.delete(stockOwned);
//
//    }
}
