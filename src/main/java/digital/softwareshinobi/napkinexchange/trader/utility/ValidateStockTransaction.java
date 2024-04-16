package digital.softwareshinobi.napkinexchange.trader.utility;

import digital.softwareshinobi.napkinexchange.broker.request.BuyStockRequest;
import digital.softwareshinobi.napkinexchange.broker.request.SellStockRequest;
import digital.softwareshinobi.napkinexchange.security.model.Security;
import digital.softwareshinobi.napkinexchange.security.exception.StockNotFoundException;
import digital.softwareshinobi.napkinexchange.security.service.SecurityService;
import digital.softwareshinobi.napkinexchange.trader.model.Trader;
import digital.softwareshinobi.napkinexchange.trader.portfolio.SecurityPosition;

public class ValidateStockTransaction {

    public static boolean doesTraderHaveEnoughAvailableBalance(Trader account,
            BuyStockRequest buyStockRequest,
            SecurityService stockService) {
        double balance = account.getAccountBalance();
        Security stock;
        try {
            stock = stockService.getStockByTickerSymbol(buyStockRequest.getTicker());
        } catch (StockNotFoundException ex) {
            return false;
        }
        return balance > (stock.getPrice() * buyStockRequest.getSharesToBuy());
    }

    public static boolean doesAccountHaveEnoughStocks(
            Trader account,
            SellStockRequest sellStockRequest) {

        SecurityPosition stock = FindStockOwned.findOwnedStockByTicker(
                account.getSecurityPortfolio(),
                sellStockRequest.getSecurity());

        if (stock == null) {

            return false;
        }

        return stock.getUnits() >= sellStockRequest.getUnits();

    }

}
