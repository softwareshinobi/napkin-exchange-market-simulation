package digital.softwareshinobi.napkinexchange.broker.service;

import digital.softwareshinobi.napkinexchange.broker.exception.TraderPortfolioException;
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
import java.util.logging.Level;
import java.util.logging.Logger;
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

    public void saveNewStockOwned(SecurityBuyRequest securityBuyRequest, Trader trader, Double price) {

        System.out.println("buying new stock for the first time for this symbol");
        System.out.println("trader / " + trader);
        System.out.println("securityBuyRequest / " + securityBuyRequest);

        SecurityPosition pos = new SecurityPosition(
                trader,
                securityBuyRequest.getTicker(),
                securityBuyRequest.getUnits(),
                price
        );

        this.securityPortfolioRepository.save(pos);

        //     trader.getSecurityPortfolio().add(pos);
        //   this.traderService.saveTraderAccount(trader);
        //      System.out.println("trader after initial buy / "+trader);
//System.out.println("trader after initial buy / "+trader);
//
//
//        try {
//            Thread.sleep(10_000);
//        } catch (InterruptedException ex) {
//            Logger.getLogger(SecurityPortfolioService.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }

    public synchronized SecurityBuyResponse buySecurityMarketPrice(SecurityBuyRequest securityBuyRequest) {

        System.out.println("enter > buySecurityMarketPrice");
        System.out.println("param > securityBuyRequest / " + securityBuyRequest);

        this.notificationService.save(new Notification(
                securityBuyRequest.getUsername(),
                NotificationType.MARKET_BUY_ORDER_CREATED,
                securityBuyRequest.toString()
        ));

        System.out.println("trader to load / " + securityBuyRequest.getUsername());

        final Trader trader = this.traderService.getAccountByName(securityBuyRequest.getUsername());

        System.out.println("loaded trader / " + trader);

        ////
        final Security security = this.securityService.getSecurityBySymbol(securityBuyRequest.getTicker());
        System.out.println("security / " + security);

        ///
        if (!ValidateStockTransaction.doesTraderHaveEnoughAvailableBalance(trader, securityBuyRequest, this.securityService)) {

            this.notificationService.save(new Notification(
                    trader.getUsername(),
                    NotificationType.TRADER_INSUFFICIENT_FUNDS,
                    securityBuyRequest.toString()
            ));

            throw new TraderBalanceException("Trader does not have funds for this purchase");

        } else {

            System.out.println("Trader has enough free cash to make this purchase. continuing...");

        }

        /////////////////////////
//        this.traderService.updateBalanceAndSave(
//                trader,
//                -1 * (securityBuyRequest.getUnits() * security.getPrice()));
        final SecurityPosition traderSecurityPosition = this.findStockOwned(trader, security);
        
        System.out.println("traderSecurityPosition / " + traderSecurityPosition);

        System.out.println("trader / before updating units / " + trader);
        
        if (traderSecurityPosition == null) {

            System.out.println("trader DOES NOT own any of this security");

            this.saveNewStockOwned(securityBuyRequest, trader, security.getPrice());

        } else {

            System.out.println("trader already OWNS UNITS of this security");

            traderSecurityPosition.updateCostBasisAndAmountOwned(
                    securityBuyRequest.getUnits(),
                    security.getPrice());

            this.securityPortfolioRepository.save(traderSecurityPosition);

        }

        System.out.println("555");

        System.out.println("trader / after updating units / " + trader);

//        try {
//            System.out.println("sleeping");
//            Thread.sleep(10000);
//        } catch (InterruptedException ex) {
//            Logger.getLogger(SecurityPortfolioService.class.getName()).log(Level.SEVERE, null, ex);
//        }

        // update the balances here
        final double transactionValue = securityBuyRequest.getUnits() * security.getPrice();
        System.out.println("transactionValue / " + transactionValue);

// subtract money from their available.
        double currentBalance = trader.getAvailableFunds();
        System.out.println("currentBalance / " + currentBalance);

        System.out.println("transactionValue / " + transactionValue);

        double afterBalance = currentBalance - transactionValue;
        System.out.println("afterBalance / " + afterBalance);

        trader.setAvailableFunds(afterBalance);

        System.out.println("trader final1 / " + trader);

        ////        
        System.out.println("updating the portfolio value");

        this.updateTraderPortfolioValues();

        ////
        System.out.println("trader final2 / " + trader);

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

    public synchronized SecuritySellResponse sellSecurityMarketPrice(SecuritySellRequest securitySellRequest) {

        for (int i = 0; i < 100; i++) {
            System.out.println();
        }

        System.out.println("enter > sellSecurityMarketPrice");

        System.out.println("sellStockRequest / " + securitySellRequest);

        notificationService.save(new Notification(
                securitySellRequest.getUsername(),
                NotificationType.MARKET_SELL_ORDER_CREATED,
                securitySellRequest.toString()
        ));

        System.out.println("trader to load / " + securitySellRequest.getUsername());

        Trader trader = traderService.getAccountByName(securitySellRequest.getUsername());

        System.out.println("loaded trader / " + trader);

        ////
                System.out.println("2 / looking up security from repository");

        final Security security = this.securityService.getSecurityBySymbol(securitySellRequest.getSecurity());
        System.out.println("security / " + security);

        ///
        if (!ValidateStockTransaction.doesAccountHaveEnoughStocks(trader, securitySellRequest)) {

            this.notificationService.save(new Notification(
                    securitySellRequest.getUsername(),
                    NotificationType.TRADER_INSUFFICIENT_SECURITY_HOLDINGS,
                    securitySellRequest.toString()
            ));
            throw new TraderPortfolioException("Account does not own enough stocks");

        } else {

            System.out.println("user has enough units of security. continuing...");

        }


        System.out.println("3 / finding security position for user by security");

        SecurityPosition securityPosition = findStockOwned(trader, security);
        System.out.println("securityPosition / " + securityPosition);

        //       System.out.println("3");
//        trader.updateTotalProfits(
//                securityPosition.getCostBasis(),
//                sellStockRequest.getUnits(),
//                security.getPrice());
        //    System.out.println("4");
        double sellValue = securitySellRequest.getUnits() * security.getPrice();
        System.out.println("sellValue / " + sellValue);

        double purchaseValue = securitySellRequest.getUnits() * securityPosition.getCostBasis();
        System.out.println("purchaseValue / " + purchaseValue);

        double profit = sellValue - purchaseValue;
        System.out.println("profit / " + profit);

        double accountAdjustment = security.getPrice() * securitySellRequest.getUnits();

        Map sellRequestMAP = new HashMap();

        sellRequestMAP.put("trader", trader);
        sellRequestMAP.put("sellValue", sellValue);
        sellRequestMAP.put("purchaseValue", purchaseValue);

        sellRequestMAP.put("units", securitySellRequest.getUnits());

        sellRequestMAP.put("price", security.getPrice());

        sellRequestMAP.put("basis", securityPosition.getCostBasis());
        sellRequestMAP.put("profit", profit);

        this.notificationService.save(new Notification(
                securitySellRequest.getUsername(),
                NotificationType.MARKET_SELL_ORDER_FULFILLED,
                sellRequestMAP.toString()
        ));

        System.out.println("// getting the units reserved");

        if (securitySellRequest.getUnits() - securityPosition.getUnits() == 0) {

            System.out.println("after this sell, no more units. so deleting security position");

            System.out.println("security position / deletion / " + securityPosition);

            System.out.println("trader / before / " + trader);

            System.out.println("repo / before / " + this.securityPortfolioRepository.findAll());

            this.securityPortfolioRepository.delete(securityPosition);

            System.out.println("trader / after / " + trader);

            System.out.println("repo / after / " + this.securityPortfolioRepository.findAll());

        } else {

            System.out.println("after sell, user will have more stock. so updating units");

            System.out.println("security position / before / " + securityPosition);

            securityPosition.setUnits(securityPosition.getUnits() - securitySellRequest.getUnits());

            this.securityPortfolioRepository.save(securityPosition);

            System.out.println("security position / after " + securityPosition);

        }

     //   this.updateTraderPortfolioValues();

//                double currentAvailableCash = trader.getAvailableFunds();
//        System.out.println("currentAvailableCash / " + currentAvailableCash);
//        
//                 System.out.println("updating the user values");
//  System.out.println("trader final1 / " + trader);
//  
//    //   trader.setAvailableFunds(currentAvailableCash+sellValue);
//  
//       System.out.println("trader final3 / " + trader);
        // update the balances here
        //     final double transactionValue = securitySellRequest.getUnits() * security.getPrice();
        //     System.out.println("transactionValue / " + transactionValue);
// subtract money from their available.
        /*
System.out.println("profit / " + profit);

        double afterBalance = currentAvailableCash + profit;
        
        System.out.println("afterBalance / " + afterBalance);

        //trader.setAvailableFunds(afterBalance);

        System.out.println("updating the portfolio value");
       this.updateTraderPortfolioValues();

        System.out.println("trader final2 / " + trader);
         */
//          this.      traderService.updateBalanceAndSave(trader, accountAdjustment);
        this.notificationService.save(new Notification(
                securitySellRequest.getUsername(),
                NotificationType.MARKET_SELL_ORDER_FULFILLED,
                securitySellRequest.toString()
        ));

        System.out.println("returning / " + securitySellRequest);

        System.out.println("exit < sellMarketPrice");

        return null;

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

    public synchronized void updateTraderPortfolioValues() {

        System.out.println("enter > updateTraderPortfolioValues");

        for (final Trader trader : this.traderService.findAllAccounts()) {

            System.out.println("trader / " + trader);

            //     Double portfolioValue = 0d;
            final List<SecurityPosition> securityPortfolio = this.findStockOwned(trader);

            for (SecurityPosition securityPosition : securityPortfolio) {

                System.out.println("securityPosition / " + securityPosition);
                //  String ticker  = ;

                Security security = this.securityService.getSecurityBySymbol(securityPosition.symbol);

                Integer units = securityPosition.units;

                Double securityPositionValue = security.getPrice() * units;

                System.out.println("    securityPositionValue / " + securityPositionValue);

                securityPosition.setValue(securityPositionValue);

                this.securityPortfolioRepository.save(securityPosition);

                //   portfolioValue = portfolioValue + securityPositionValue;
            }

            System.out.println("updating the trader's portfolio value");

        }

    }

//    public Double calculatePortfolioValue(Trader trader) {
//
//        List<SecurityPosition> securityPortfolio = this.findStockOwned(trader);
//
//        ///   System.out.println("security portfolio for trader / " + trader.getUsername());
//        //   System.out.println(securityPortfolio);
//        double securityPortfolioValue = 0;
//
//        for (SecurityPosition securityPosition : securityPortfolio) {
//
//            //       System.out.println("position / " + securityPosition);
//            Security security = this.securityService.getSecurityBySymbol(securityPosition.symbol);
//
//            securityPortfolioValue = securityPortfolioValue + (security.getPrice() * securityPosition.units);
//
//        }
//
//        //    System.out.println("portfolio value / " + securityPortfolioValue);
//        return securityPortfolioValue;
//
//    }
}
