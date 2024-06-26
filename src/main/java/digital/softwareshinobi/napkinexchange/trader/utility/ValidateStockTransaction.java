package digital.softwareshinobi.napkinexchange.trader.utility;

import digital.softwareshinobi.napkinexchange.broker.request.SecurityBuyRequest;
import digital.softwareshinobi.napkinexchange.broker.request.SecuritySellRequest;
import digital.softwareshinobi.napkinexchange.security.model.Security;
import digital.softwareshinobi.napkinexchange.security.exception.SecurityNotFoundException;
import digital.softwareshinobi.napkinexchange.security.service.SecurityService;
import digital.softwareshinobi.napkinexchange.trader.model.Trader;
import digital.softwareshinobi.napkinexchange.trader.portfolio.SecurityPosition;

public class ValidateStockTransaction {

    public static boolean doesTraderHaveEnoughAvailableBalance(
            Trader trader,
            SecurityBuyRequest securityBuyRequest,
            SecurityService securityService) {
        
        double balance = trader.getAccountBalance();
        
        Security security;
        
        try {
        
            security = securityService.getSecurityBySymbol(securityBuyRequest.getTicker());
        
        } catch (SecurityNotFoundException securityNotFoundException) {
        
            return false;
        
        }
        
        return balance > (security.getPrice() * securityBuyRequest.getUnits());
    }

    public static boolean doesAccountHaveEnoughStocks(
            Trader trader,
            SecuritySellRequest securitySellRequest) {

        SecurityPosition securityPosition = FindStockOwned.findOwnedStockByTicker(
                trader.getSecurityPortfolio(),
                securitySellRequest.getSecurity());

        System.out.println("securityPosition / "+securityPosition);
                System.out.println("securitySellRequest / "+securitySellRequest);

        if (securityPosition == null) {

            return false;
        }

      boolean hasEnough= securityPosition.getUnits() >= securitySellRequest.getUnits();

                      System.out.println("hasEnough / "+hasEnough);

                      
      return hasEnough;
    }

}
