package digital.softwareshinobi.napkinexchange.account.utils;

import digital.softwareshinobi.napkinexchange.stocks.stock.exception.StockNotFoundException;
import digital.softwareshinobi.napkinexchange.stocks.stock.entity.Stock;
import digital.softwareshinobi.napkinexchange.stocks.stock.service.StockService;
import digital.softwareshinobi.napkinexchange.account.model.entity.Account;
import digital.softwareshinobi.napkinexchange.account.model.entity.StockOwned;
import digital.softwareshinobi.napkinexchange.account.model.payload.BuyStockRequest;
import digital.softwareshinobi.napkinexchange.account.model.payload.SellStockRequest;

public class ValidateStockTransaction {

    public static boolean doesAccountHaveEnoughMoney(Account account,
            BuyStockRequest buyStockRequest,
            StockService stockService) {
        double balance = account.getAccountBalance();
        Stock stock;
        try {
            stock = stockService.getStockByTickerSymbol(buyStockRequest.getTicker());
        } catch (StockNotFoundException ex) {
            return false;
        }
        return balance > (stock.getPrice() * buyStockRequest.getSharesToBuy());
    }

    public static boolean doesAccountHaveEnoughStocks(Account account,
            SellStockRequest sellStock) {
        StockOwned stock = FindStockOwned.findOwnedStockByTicker(
                account.getStocksOwned(), sellStock.getTicker());
        if (stock == null) {
            return false;
        }
        return stock.getAmountOwned() >= sellStock.getSharesToSell();
    }
}
