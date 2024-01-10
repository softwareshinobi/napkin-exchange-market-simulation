package digital.softwareshinobi.napkinexchange.inventory.controller;

import lombok.AllArgsConstructor;
import digital.softwareshinobi.napkinexchange.stocks.stock.entity.Stock;
import digital.softwareshinobi.napkinexchange.stocks.stock.service.StockService;
import digital.softwareshinobi.napkinexchange.account.exception.AccountBalanceException;
import digital.softwareshinobi.napkinexchange.account.model.entity.Account;
import digital.softwareshinobi.napkinexchange.account.model.entity.StockOwned;
import digital.softwareshinobi.napkinexchange.account.model.payload.BuyStockRequest;
import digital.softwareshinobi.napkinexchange.account.model.payload.SellStockRequest;
import digital.softwareshinobi.napkinexchange.account.repository.StockOwnedRepository;
import digital.softwareshinobi.napkinexchange.account.service.AccountService;
import digital.softwareshinobi.napkinexchange.account.utils.ValidateStockTransaction;
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

    public void fillBuyStockRequest(BuyStockRequest buyStockRequest) {

        System.out.println("enter > placeAssetBuyMarketOrder");

        System.out.println("buyStockRequest / " + buyStockRequest);

        Account account = accountService.getAccountByName(buyStockRequest.getUsername());

        Stock stock = stockService.getStockByTickerSymbol(buyStockRequest.getTicker());

        StockOwned stockOwned = findStockOwned(account, stock);

        if (!ValidateStockTransaction.doesAccountHaveEnoughMoney(account, buyStockRequest, this.stockService)) {

            throw new AccountBalanceException("Account does not have funds for this purchase");

        }

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

    public void saveNewStockOwned(BuyStockRequest buyStock, Account account, double stockPrice) {

        stockOwnedRepository.save(new StockOwned(
                account,
                buyStock.getTicker(),
                buyStock.getSharesToBuy(),
                stockPrice
        ));

    }

    public void sellStockMarketPrice(SellStockRequest sellStock) {

        System.out.println("sellStocksellStocksellStock");

        Account account = accountService.getAccountByName(sellStock.getUsername());

        System.out.println("1");

        if (!ValidateStockTransaction.doesAccountHaveEnoughStocks(account, sellStock)) {

            throw new AccountInventoryException("Account does not own enough stocks");

        }

        System.out.println("2.0");
     
        Stock stock = stockService.getStockByTickerSymbol(sellStock.getTicker());

        System.out.println("2");

        StockOwned stockOwned = findStockOwned(account, stock);

        System.out.println("3");

        account.updateTotalProfits(
                stockOwned.getCostBasis(),
                sellStock.getSharesToSell(),
                stock.getPrice());

        System.out.println("4");

        accountService.updateBalanceAndSave(account, stock.getPrice() * sellStock.getSharesToSell());

        System.out.println("5");

        if (sellStock.getSharesToSell() - stockOwned.getAmountOwned() == 0) {

            System.out.println("6");

            clearAndDeleteStockOwned(stockOwned);

        } else {

            System.out.println("7");

            stockOwned.setAmountOwned(stockOwned.getAmountOwned() - sellStock.getSharesToSell());

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

    public void clearAndDeleteStockOwned(StockOwned stockOwned) {

        stockOwnedRepository.delete(stockOwned);

    }

}
