package digital.softwareshinobi.napkinexchange.trader.utility;

import digital.softwareshinobi.napkinexchange.broker.request.BuyStockRequest;
import digital.softwareshinobi.napkinexchange.broker.request.SellStockRequest;
import digital.softwareshinobi.napkinexchange.security.model.Stock;
import digital.softwareshinobi.napkinexchange.security.exception.StockNotFoundException;
import digital.softwareshinobi.napkinexchange.security.service.StockService;
import digital.softwareshinobi.napkinexchange.trader.model.Account;
import digital.softwareshinobi.napkinexchange.trader.model.StockOwned;

public class ValidateStockTransaction {

    public static boolean doesTraderHaveEnoughAvailableBalance(Account account,
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

    public static boolean doesAccountHaveEnoughStocks(
            Account account,
            SellStockRequest sellStockRequest) {

        StockOwned stock = FindStockOwned.findOwnedStockByTicker(
                account.getStocksOwned(),
                sellStockRequest.getSecurity());

        if (stock == null) {

            return false;
        }

        return stock.getAmountOwned() >= sellStockRequest.getUnits();

    }

}
