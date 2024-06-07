package digital.softwareshinobi.napkinexchange.broker.service;

import digital.softwareshinobi.napkinexchange.broker.exception.AccountInventoryException;
import digital.softwareshinobi.napkinexchange.broker.request.SecurityBuyRequest;
import digital.softwareshinobi.napkinexchange.broker.request.SecuritySellRequest;
import digital.softwareshinobi.napkinexchange.notification.model.Notification;
import digital.softwareshinobi.napkinexchange.notification.model.NotificationType;
import digital.softwareshinobi.napkinexchange.notification.service.NotificationService;
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

@Service
@AllArgsConstructor
public class SecurityPortfolioService {

    @Autowired
    private final SecurityPortfolioRepository securityPortfolioRepository;

    @Autowired
    private final TraderService traderService;

    @Autowired
    private final SecurityService securityService;

    @Autowired
    private final NotificationService notificationService;

    public void fillBuyMarketStockRequest(SecurityBuyRequest securityBuyRequest) {

        //  System.out.println("enter > fillBuyMarketStockRequest");
        //      System.out.println("buyStockRequest / " + securityBuyRequest);
        this.notificationService.save(
                new Notification(
                        securityBuyRequest.getUsername(),
                        NotificationType.MARKET_BUY_ORDER_CREATED,
                        securityBuyRequest.toString()
                ));

        final Trader traderAccount = this.traderService.getAccountByName(securityBuyRequest.getUsername());

        final Security securityToBuy = this.securityService.getSecurityBySymbol(securityBuyRequest.getTicker());

        final SecurityPosition traderSecurityPosition = this.findStockOwned(traderAccount, securityToBuy);

        if (!ValidateStockTransaction.doesTraderHaveEnoughAvailableBalance(traderAccount, securityBuyRequest, this.securityService)) {

            this.notificationService.save(
                    new Notification(
                            securityBuyRequest.getUsername(),
                            NotificationType.MARKET_BUY_INSUFFICIENT_FUNDS,
                            securityBuyRequest.toString()
                    ));

            throw new TraderBalanceException("Account does not have funds for this purchase");

        }

        /////////////////////////
        this.traderService.updateBalanceAndSave(
                traderAccount,
                -1 * (securityBuyRequest.getUnits() * securityToBuy.getPrice()));

        if (traderSecurityPosition == null) {

            //       System.out.println("trader DOES NOT own any of this security");
            //stock IS CURRENTLY owned by the user: "+ securityBuyRequest.getUsername());
            //    traderService.updateBalanceAndSave(traderAccount, -1 * (securityBuyRequest.getUnits() * securityToBuy.getPrice()));
            //   traderSecurityPosition.updateCostBasisAndAmountOwned(securityBuyRequest.getUnits(), securityToBuy.getPrice());
            this.saveNewStockOwned(securityBuyRequest, traderAccount, securityToBuy.getPrice());

        } else {

            //  System.out.println("trader OWNS UNITS of this security");
            //subtract transaction value from account balance
            //    System.out.println("before / " + traderSecurityPosition);
            traderSecurityPosition.updateCostBasisAndAmountOwned(
                    securityBuyRequest.getUnits(),
                    securityToBuy.getPrice());

            //    System.out.println("after / " + traderSecurityPosition);
            this.securityPortfolioRepository.save(traderSecurityPosition);

        }

        this.notificationService.save(
                new Notification(
                        securityBuyRequest.getUsername(),
                        NotificationType.MARKET_BUY_ORDER_FULFILLED,
                        securityBuyRequest.toString()
                ));

        //  System.out.println("doing done order things");
    }

    public void saveNewStockOwned(SecurityBuyRequest buyStockRequest, Trader account, double stockPrice) {

        this.securityPortfolioRepository.save(
                new SecurityPosition(
                        account,
                        buyStockRequest.getTicker(),
                        buyStockRequest.getUnits(),
                        stockPrice
                ));

//        notificationService.save(
//                new Notification(
//                        buyStockRequest.getUsername(),
//                        NotificationType.LONGBUY,
//                        "BOUGHT NEW STOCK / " + buyStockRequest.toString()
//                ));
    }

    public void sellSecurityMarketPrice(SecuritySellRequest sellStockRequest) {

        System.out.println("sellStocksellStocksellStock");

        notificationService.save(
                new Notification(
                        sellStockRequest.getUsername(),
                        NotificationType.MARKET_SELL_ORDER_CREATED,
                        sellStockRequest.toString()
                ));

        Trader trader = traderService.getAccountByName(sellStockRequest.getUsername());

        System.out.println("1");

        if (!ValidateStockTransaction.doesAccountHaveEnoughStocks(trader, sellStockRequest)) {

            throw new AccountInventoryException("Account does not own enough stocks");

        }

        System.out.println("2.0");

        Security security = securityService.getSecurityBySymbol(sellStockRequest.getSecurity());

        System.out.println("2");

        SecurityPosition securityPosition = findStockOwned(trader, security);

        System.out.println("3");

        trader.updateTotalProfits(
                securityPosition.getCostBasis(),
                sellStockRequest.getUnits(),
                security.getPrice());

        System.out.println("4");

        traderService.updateBalanceAndSave(trader, security.getPrice() * sellStockRequest.getUnits());

        System.out.println("5");

        if (sellStockRequest.getUnits() - securityPosition.getUnits() == 0) {

            System.out.println("6");

            securityPortfolioRepository.delete(securityPosition);
            //  clearAndDeleteStockOwned(securityPosition);
        } else {

            System.out.println("7");

            securityPosition.setUnits(securityPosition.getUnits() - sellStockRequest.getUnits());

            securityPortfolioRepository.save(securityPosition);

        }

        this.notificationService.save(
                new Notification(
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
