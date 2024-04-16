package digital.softwareshinobi.napkinexchange.broker.service;

import digital.softwareshinobi.napkinexchange.broker.exception.AccountInventoryException;
import digital.softwareshinobi.napkinexchange.broker.request.BuyStockRequest;
import digital.softwareshinobi.napkinexchange.broker.request.SellStockRequest;
import digital.softwareshinobi.napkinexchange.notification.model.Notification;
import digital.softwareshinobi.napkinexchange.notification.model.NotificationType;
import digital.softwareshinobi.napkinexchange.notification.service.NotificationService;
import digital.softwareshinobi.napkinexchange.security.model.Security;
import digital.softwareshinobi.napkinexchange.security.service.SecurityService;
import digital.softwareshinobi.napkinexchange.trader.exception.AccountBalanceException;
import digital.softwareshinobi.napkinexchange.trader.model.Trader;
import digital.softwareshinobi.napkinexchange.trader.portfolio.SecurityPosition;
import digital.softwareshinobi.napkinexchange.trader.repository.StockOwnedRepository;
import digital.softwareshinobi.napkinexchange.trader.service.AccountService;
import digital.softwareshinobi.napkinexchange.trader.utility.ValidateStockTransaction;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class SecurityPortfolioService {

    @Autowired
    private final StockOwnedRepository stockOwnedRepository;

    @Autowired
    private final AccountService traderService;

    @Autowired
    private final SecurityService securityService;

    @Autowired
    private final NotificationService notificationService;

    public void fillBuyMarketStockRequest(BuyStockRequest buyStockRequest) {

        System.out.println("enter > fillBuyMarketStockRequest");

        notificationService.save(
                new Notification(
                        buyStockRequest.getUsername(),
                        NotificationType.MARKET_BUY_ORDER_CREATED,
                        buyStockRequest.toString()
                ));

        System.out.println("buyStockRequest / " + buyStockRequest);

        Trader traderAccount = this.traderService.getAccountByName(buyStockRequest.getUsername());

        Security securityToBuy = this.securityService.getStockByTickerSymbol(buyStockRequest.getTicker());

        SecurityPosition stockOwnedByUser = this.findStockOwned(traderAccount, securityToBuy);

        if (!ValidateStockTransaction.doesTraderHaveEnoughAvailableBalance(traderAccount, buyStockRequest, this.securityService)) {

            notificationService.save(
                    new Notification(
                            buyStockRequest.getUsername(),
                            NotificationType.MARKET_BUY_INSUFFICIENT_FUNDS,
                            buyStockRequest.toString()
                    ));

            throw new AccountBalanceException("Account does not have funds for this purchase");

        }

        if (stockOwnedByUser != null) {

            //subtract transaction value from account balance
            traderService.updateBalanceAndSave(traderAccount, -1 * (buyStockRequest.getSharesToBuy() * securityToBuy.getPrice()));

            stockOwnedByUser.updateCostBasisAndAmountOwned(buyStockRequest.getSharesToBuy(), securityToBuy.getPrice());

            stockOwnedRepository.save(stockOwnedByUser);

            notificationService.save(
                    new Notification(
                            buyStockRequest.getUsername(),
                            NotificationType.MARKET_BUY_ORDER_FULFILLED,
                            buyStockRequest.toString()
                    ));

            return;

        }

        traderService.updateBalanceAndSave(traderAccount, -1 * (buyStockRequest.getSharesToBuy() * securityToBuy.getPrice()));

        saveNewStockOwned(buyStockRequest, traderAccount, securityToBuy.getPrice());

        notificationService.save(
                new Notification(
                        buyStockRequest.getUsername(),
                        NotificationType.MARKET_BUY_ORDER_FULFILLED,
                        buyStockRequest.toString()
                ));

    }

    public void saveNewStockOwned(BuyStockRequest buyStockRequest, Trader account, double stockPrice) {

        stockOwnedRepository.save(new SecurityPosition(
                account,
                buyStockRequest.getTicker(),
                buyStockRequest.getSharesToBuy(),
                stockPrice
        ));

//        notificationService.save(
//                new Notification(
//                        buyStockRequest.getUsername(),
//                        NotificationType.LONGBUY,
//                        "BOUGHT NEW STOCK / " + buyStockRequest.toString()
//                ));
    }

    public void sellStockMarketPrice(SellStockRequest sellStockRequest) {

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

        Security security = securityService.getStockByTickerSymbol(sellStockRequest.getSecurity());

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

            stockOwnedRepository.delete(securityPosition);
            //  clearAndDeleteStockOwned(securityPosition);
        } else {

            System.out.println("7");

            securityPosition.setUnits(securityPosition.getUnits() - sellStockRequest.getUnits());

            stockOwnedRepository.save(securityPosition);

        }

        this.notificationService.save(
                new Notification(
                        sellStockRequest.getUsername(),
                        NotificationType.MARKET_SELL_ORDER_FULFILLED,
                        sellStockRequest.toString()
                ));

    }

    public List<SecurityPosition> findStockOwned(Trader trader) {

        return stockOwnedRepository.findAll().stream()
                .filter(securityPosition -> securityPosition.getTrader().getUsername().equals(trader.getUsername()))
                .collect(Collectors.toList());

    }

    public SecurityPosition findStockOwned(Trader trader, Security security) {

        return stockOwnedRepository.findAll().stream()
                .filter(securityPosition -> securityPosition.getSymbol().equalsIgnoreCase(security.getTicker()))
                .filter(securityPosition -> securityPosition.getTrader().getUsername().equals(trader.getUsername()))
                .findFirst()
                .orElse(null);

    }

    public Double calculatePortfolioValue(Trader trader) {
        
    
     List<SecurityPosition> securityPortfolio = this.findStockOwned(trader);

            System.out.println("security portfolio for trader / " + trader.getUsername());

         //   System.out.println(securityPortfolio);

            double securityPortfolioValue = 0;
            
            for (SecurityPosition securityPosition : securityPortfolio) {

                System.out.println("position / " + securityPosition);

                Security security = this.securityService.getStockByTickerSymbol(securityPosition.symbol);

                securityPortfolioValue = securityPortfolioValue + (security.getPrice() * securityPosition.units);

            }

            System.out.println("portfolio value / " + securityPortfolioValue);
            
            return securityPortfolioValue;
            
    }
    
}
