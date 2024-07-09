package digital.softwareshinobi.napkinexchange.trader.utility;

import digital.softwareshinobi.napkinexchange.broker.request.SecurityBuyRequest;
import digital.softwareshinobi.napkinexchange.broker.request.SecuritySellRequest;
import digital.softwareshinobi.napkinexchange.security.model.Security;
import digital.softwareshinobi.napkinexchange.security.exception.SecurityNotFoundException;
import digital.softwareshinobi.napkinexchange.security.service.SecurityService;
import digital.softwareshinobi.napkinexchange.trader.model.Trader;
import digital.softwareshinobi.napkinexchange.trader.portfolio.SecurityPosition;
import digital.softwareshinobi.napkinexchange.trader.service.TraderHistoryService;
import org.slf4j.LoggerFactory;

public class ValidateStockTransaction {
                        
    private final org.slf4j.Logger logger = LoggerFactory.getLogger(ValidateStockTransaction.class);

    public static boolean doesTraderHaveEnoughAvailableBalance(
            Trader trader,
            SecurityBuyRequest securityBuyRequest,
            SecurityService securityService) {

        double balance = trader.getAccountBalance();

        Security security;

        try {

            security = securityService.getSecurityByTicker(securityBuyRequest.getTicker());

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

        logger.debug("securityPosition / " + securityPosition);
        logger.debug("securitySellRequest / " + securitySellRequest);

        if (securityPosition == null) {

            return false;
        }

        boolean hasEnough = securityPosition.getUnits() >= securitySellRequest.getUnits();

        logger.debug("hasEnough / " + hasEnough);

        return hasEnough;
    }

}
