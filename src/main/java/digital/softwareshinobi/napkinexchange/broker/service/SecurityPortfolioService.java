package digital.softwareshinobi.napkinexchange.broker.service;

import digital.softwareshinobi.napkinexchange.broker.controller.BrokerOrderController;
import digital.softwareshinobi.napkinexchange.broker.exception.AccountInventoryException;
import digital.softwareshinobi.napkinexchange.broker.request.SecurityBuyRequest;
import digital.softwareshinobi.napkinexchange.broker.request.SecuritySellRequest;
import digital.softwareshinobi.napkinexchange.broker.response.SecurityBuyResponse;
import digital.softwareshinobi.napkinexchange.notification.model.Notification;
import digital.softwareshinobi.napkinexchange.notification.service.NotificationService;
import digital.softwareshinobi.napkinexchange.notification.type.NotificationType;
import digital.softwareshinobi.napkinexchange.security.model.Security;
import digital.softwareshinobi.napkinexchange.security.service.SecurityService;
import digital.softwareshinobi.napkinexchange.trader.exception.TraderBalanceException;
import digital.softwareshinobi.napkinexchange.trader.model.Trader;
import digital.softwareshinobi.napkinexchange.trader.portfolio.SecurityPosition;
import digital.softwareshinobi.napkinexchange.trader.service.TraderService;
import digital.softwareshinobi.napkinexchange.trader.utility.ValidateStockTransaction;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import digital.softwareshinobi.napkinexchange.trader.repository.SecurityPortfolioRepository;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
@Transactional
public class SecurityPortfolioService {

    private final org.slf4j.Logger logger = LoggerFactory.getLogger(SecurityPortfolioService.class);

    @Autowired
    private final SecurityPortfolioRepository securityPortfolioRepository;

    @Autowired
    private final TraderService traderService;

    @Autowired
    private final SecurityService securityService;

    @Autowired
    private final NotificationService notificationService;

    public SecurityBuyResponse buyMarketPrice(SecurityBuyRequest securityBuyRequest) {

        logger.debug("enter > buyMarketPrice");

        logger.debug("securityBuyRequest / " + securityBuyRequest);

        this.notificationService.save(new Notification(
                securityBuyRequest.getUsername(),
                NotificationType.MARKET_BUY_ORDER_CREATED,
                securityBuyRequest.toString()
        ));

        logger.debug("username to load / " + securityBuyRequest.getUsername());

        final Trader trader = this.traderService.getAccountByName(securityBuyRequest.getUsername());

        logger.debug("loaded trader / " + trader);
        final Security security = this.securityService.getSecurityByTicker(securityBuyRequest.getTicker());

        final SecurityPosition traderSecurityPosition = this.findStockOwned(trader, security);

        if (!ValidateStockTransaction.doesTraderHaveEnoughAvailableBalance(trader, securityBuyRequest, this.securityService)) {

            this.notificationService.save(new Notification(
                    securityBuyRequest.getUsername(),
                    NotificationType.MARKET_BUY_INSUFFICIENT_FUNDS,
                    securityBuyRequest.toString()
            ));

            throw new TraderBalanceException("Trader does not have funds for this purchase");

        }

        /////////////////////////
        this.traderService.updateBalanceAndSave(
                trader,
                -1 * (securityBuyRequest.getUnits() * security.getPrice()));

        if (traderSecurityPosition == null) {

            logger.debug("trader DOES NOT own any of this security");

            //stock IS CURRENTLY owned by the user: "+ securityBuyRequest.getUsername());
            //    traderService.updateBalanceAndSave(traderAccount, -1 * (securityBuyRequest.getUnits() * securityToBuy.getPrice()));
            //   traderSecurityPosition.updateCostBasisAndAmountOwned(securityBuyRequest.getUnits(), securityToBuy.getPrice());
            this.saveNewStockOwned(securityBuyRequest, trader, security.getPrice());

        } else {

            logger.debug("trader does OWNS UNITS of this security");

//subtract transaction value from account balance
            //    logger.debug("before / " + traderSecurityPosition);
            traderSecurityPosition.updateCostBasisAndAmountOwned(
                    securityBuyRequest.getUnits(),
                    security.getPrice());

            //    logger.debug("after / " + traderSecurityPosition);
            this.securityPortfolioRepository.save(traderSecurityPosition);

        }

        logger.debug("openSimpleBuyOrder / fulfilled");

        SecurityBuyResponse securityBuyResponse = new SecurityBuyResponse(securityBuyRequest, security);

        this.notificationService.save(new Notification(
                securityBuyResponse.getTrader(),
                NotificationType.MARKET_BUY_ORDER_FULFILLED,
                securityBuyResponse.toString()
        ));

        logger.debug("returning / " + securityBuyResponse);

        logger.debug("exit < buyMarketPrice");

        return securityBuyResponse;

    }

    public void saveNewStockOwned(SecurityBuyRequest securityBuyRequest, Trader trader, double price) {

        this.securityPortfolioRepository.save(
                new SecurityPosition(
                        trader,
                        securityBuyRequest.getTicker(),
                        securityBuyRequest.getUnits(),
                        price
                ));

    }

    public void sellSecurityMarketPrice(SecuritySellRequest sellStockRequest) {

        logger.debug("enter > sellSecurityMarketPrice");

        logger.debug("sellStockRequest / " + sellStockRequest);

        notificationService.save(new Notification(
                sellStockRequest.getUsername(),
                NotificationType.MARKET_SELL_ORDER_CREATED,
                sellStockRequest.toString()
        ));

        Trader trader = traderService.getAccountByName(sellStockRequest.getUsername());

        logger.debug("trader / " + trader);

//        if (!ValidateStockTransaction.doesAccountHaveEnoughStocks(trader, sellStockRequest)) {
//
//            throw new AccountInventoryException("Account does not own enough stocks");
//
//        }
        logger.debug("2 / looking up security from repository");

        Security security = securityService.getSecurityByTicker(sellStockRequest.getSecurity());

        logger.debug("security / " + security);

        logger.debug("3 / finding security position for user by security");

        SecurityPosition securityPosition = findStockOwned(trader, security);
        logger.debug("securityPosition / " + securityPosition);

        logger.debug("3");

        trader.updateTotalProfits(
                securityPosition.getCostBasis(),
                sellStockRequest.getUnits(),
                security.getPrice());

        logger.debug("4");

        double sellValue = sellStockRequest.getUnits() * security.getPrice();
        logger.debug("sellValue / " + sellValue);

        double purchaseValue = sellStockRequest.getUnits() * securityPosition.getCostBasis();
        logger.debug("purchaseValue / " + purchaseValue);

        double profit = sellValue - purchaseValue;
        logger.debug("profit / " + profit);

        Map as = new HashMap();

        as.put("sellValue", sellValue);
        as.put("purchaseValue", purchaseValue);

        as.put("units", sellStockRequest.getUnits());

        as.put("price", security.getPrice());

        as.put("basis", securityPosition.getCostBasis());
        as.put("profit", profit);

        this.notificationService.save(new Notification(
                sellStockRequest.getUsername(),
                NotificationType.MARKET_SELL_ORDER_FULFILLED,
                as.toString()
        ));

        traderService.updateBalanceAndSave(trader, security.getPrice() * sellStockRequest.getUnits());

        logger.debug("5");

        if (sellStockRequest.getUnits() - securityPosition.getUnits() == 0) {

            logger.debug("after this sell, no more units. so deleting security position");

            logger.debug("security position / deletion / " + securityPosition);

            logger.debug("trader / before / " + trader);

            logger.debug("repo / before / " + this.securityPortfolioRepository.findAll());
            this.securityPortfolioRepository.delete(securityPosition);

            logger.debug("trader / after / " + trader);

            logger.debug("repo / after / " + this.securityPortfolioRepository.findAll());

        } else {

            logger.debug("after sell, user will have more stock. so updating units");

            System.out.append("security position / before / " + securityPosition);

            securityPosition.setUnits(securityPosition.getUnits() - sellStockRequest.getUnits());

            this.securityPortfolioRepository.save(securityPosition);

            System.out.append("security position / after " + securityPosition);

        }

        this.notificationService.save(new Notification(
                sellStockRequest.getUsername(),
                NotificationType.MARKET_SELL_ORDER_FULFILLED,
                sellStockRequest.toString()
        ));

    }

    public List<SecurityPosition> findStockOwned(Trader trader) {

        return this.securityPortfolioRepository.findAll().stream()
                .filter(securityPosition -> securityPosition.getTrader().getUsername().equals(trader.getUsername()))
                .collect(Collectors.toList());

    }

    public SecurityPosition findStockOwned(Trader trader, Security security) {

        return this.securityPortfolioRepository.findAll().stream()
                .filter(securityPosition -> securityPosition.getSymbol().equalsIgnoreCase(security.getTicker()))
                .filter(securityPosition -> securityPosition.getTrader().getUsername().equals(trader.getUsername()))
                .findFirst()
                .orElse(null);

    }

    public Double calculatePortfolioValue(Trader trader) {

        List<SecurityPosition> securityPortfolio = this.findStockOwned(trader);

        ///   logger.debug("security portfolio for trader / " + trader.getUsername());
        //   logger.debug(securityPortfolio);
        double securityPortfolioValue = 0;

        for (SecurityPosition securityPosition : securityPortfolio) {

            //       logger.debug("position / " + securityPosition);
            Security security = this.securityService.getSecurityByTicker(securityPosition.symbol);

            securityPortfolioValue = securityPortfolioValue + (security.getPrice() * securityPosition.units);

        }

        //    logger.debug("portfolio value / " + securityPortfolioValue);
        return securityPortfolioValue;

    }

}
