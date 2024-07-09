package digital.softwareshinobi.napkinexchange.broker.service;

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
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
@Transactional
public class SecurityPortfolioService {

    @Autowired
    private final SecurityPortfolioRepository securityPortfolioRepository;

    @Autowired
    private final TraderService traderService;

    @Autowired
    private final SecurityService securityService;

    @Autowired
    private final NotificationService notificationService;

    public SecurityBuyResponse buyMarketPrice(SecurityBuyRequest securityBuyRequest) {

        System.out.println("enter > buyMarketPrice");

        System.out.println("securityBuyRequest / " + securityBuyRequest);

        this.notificationService.save(new Notification(
                securityBuyRequest.getUsername(),
                NotificationType.MARKET_BUY_ORDER_CREATED,
                securityBuyRequest.toString()
        ));

        System.out.println("username to load / " + securityBuyRequest.getUsername());

        final Trader trader = this.traderService.getAccountByName(securityBuyRequest.getUsername());

        System.out.println("loaded trader / " + trader);
        final Security security = this.securityService.getSecurityBySymbol(securityBuyRequest.getTicker());

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

            System.out.println("trader DOES NOT own any of this security");

            //stock IS CURRENTLY owned by the user: "+ securityBuyRequest.getUsername());
            //    traderService.updateBalanceAndSave(traderAccount, -1 * (securityBuyRequest.getUnits() * securityToBuy.getPrice()));
            //   traderSecurityPosition.updateCostBasisAndAmountOwned(securityBuyRequest.getUnits(), securityToBuy.getPrice());
            this.saveNewStockOwned(securityBuyRequest, trader, security.getPrice());

        } else {

            System.out.println("trader does OWNS UNITS of this security");

//subtract transaction value from account balance
            //    System.out.println("before / " + traderSecurityPosition);
            traderSecurityPosition.updateCostBasisAndAmountOwned(
                    securityBuyRequest.getUnits(),
                    security.getPrice());

            //    System.out.println("after / " + traderSecurityPosition);
            this.securityPortfolioRepository.save(traderSecurityPosition);

        }

        System.out.println("openSimpleBuyOrder / fulfilled");

        SecurityBuyResponse securityBuyResponse = new SecurityBuyResponse(securityBuyRequest, security);

        this.notificationService.save(new Notification(
                securityBuyResponse.getTrader(),
                NotificationType.MARKET_BUY_ORDER_FULFILLED,
                securityBuyResponse.toString()
        ));

        System.out.println("returning / " + securityBuyResponse);

        System.out.println("exit < buyMarketPrice");

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

        System.out.println("enter > sellSecurityMarketPrice");

        System.out.println("sellStockRequest / " + sellStockRequest);

        notificationService.save(new Notification(
                sellStockRequest.getUsername(),
                NotificationType.MARKET_SELL_ORDER_CREATED,
                sellStockRequest.toString()
        ));

        Trader trader = traderService.getAccountByName(sellStockRequest.getUsername());

        System.out.println("trader / " + trader);

//        if (!ValidateStockTransaction.doesAccountHaveEnoughStocks(trader, sellStockRequest)) {
//
//            throw new AccountInventoryException("Account does not own enough stocks");
//
//        }
        System.out.println("2 / looking up security from repository");

        Security security = securityService.getSecurityBySymbol(sellStockRequest.getSecurity());

        System.out.println("security / " + security);

        System.out.println("3 / finding security position for user by security");

        SecurityPosition securityPosition = findStockOwned(trader, security);
        System.out.println("securityPosition / " + securityPosition);

        System.out.println("3");

        trader.updateTotalProfits(
                securityPosition.getCostBasis(),
                sellStockRequest.getUnits(),
                security.getPrice());

        System.out.println("4");

        double sellValue = sellStockRequest.getUnits() * security.getPrice();
        System.out.println("sellValue / " + sellValue);

        double purchaseValue = sellStockRequest.getUnits() * securityPosition.getCostBasis();
        System.out.println("purchaseValue / " + purchaseValue);

        double profit = sellValue - purchaseValue;
        System.out.println("profit / " + profit);

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

        System.out.println("5");

        if (sellStockRequest.getUnits() - securityPosition.getUnits() == 0) {

            System.out.println("after this sell, no more units. so deleting security position");

            System.out.println("security position / deletion / " + securityPosition);

            System.out.println("trader / before / " + trader);

            System.out.println("repo / before / " + this.securityPortfolioRepository.findAll());
            this.securityPortfolioRepository.delete(securityPosition);

            System.out.println("trader / after / " + trader);

            System.out.println("repo / after / " + this.securityPortfolioRepository.findAll());

        } else {

            System.out.println("after sell, user will have more stock. so updating units");

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

        ///   System.out.println("security portfolio for trader / " + trader.getUsername());
        //   System.out.println(securityPortfolio);
        double securityPortfolioValue = 0;

        for (SecurityPosition securityPosition : securityPortfolio) {

            //       System.out.println("position / " + securityPosition);
            Security security = this.securityService.getSecurityBySymbol(securityPosition.symbol);

            securityPortfolioValue = securityPortfolioValue + (security.getPrice() * securityPosition.units);

        }

        //    System.out.println("portfolio value / " + securityPortfolioValue);
        return securityPortfolioValue;

    }

}
