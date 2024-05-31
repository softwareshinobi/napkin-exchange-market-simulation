package digital.softwareshinobi.napkinexchange.trader.service;

import digital.softwareshinobi.napkinexchange.broker.service.SecurityPortfolioService;
import digital.softwareshinobi.napkinexchange.market.model.Market;
import digital.softwareshinobi.napkinexchange.market.service.MarketService;
import digital.softwareshinobi.napkinexchange.security.service.SecurityService;
import digital.softwareshinobi.napkinexchange.security.utility.SortHistory;
import digital.softwareshinobi.napkinexchange.trader.model.AccountHistory;
import digital.softwareshinobi.napkinexchange.trader.model.Trader;
import digital.softwareshinobi.napkinexchange.trader.repository.AccountHistoryRepository;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class TraderHistoryService {

    @Autowired
    private final AccountHistoryRepository accountHistoryRepository;

    @Autowired
    private final TraderService traderService;

    @Autowired
    private final MarketService marketService;

    @Autowired
    private final SecurityPortfolioService securityPortfolioService;

    @Autowired
    private final SecurityService securityService;

    public void updateTraderAccountHistory() {

        Market market = this.marketService.getMarket();

        List<Trader> traderList = this.traderService.findAllAccounts();

        for (final Trader trader : traderList) {

            double portfolioValue = this.securityPortfolioService.calculatePortfolioValue(trader);

            ////////
            AccountHistory newAccountHistory = new AccountHistory(
                    market.getDate(),
                    trader,
                    trader.getTotalProfits(),
                    portfolioValue
            );

            //    System.out.println("newAccountHistory / " + newAccountHistory);
            this.accountHistoryRepository.save(newAccountHistory);

            newAccountHistory = null;
///////////
            //  System.out.println("portfolioValue / " + portfolioValue);
            //    System.out.println("trader / before / " + trader);

            trader.setPortfolioValue(portfolioValue);
            trader.setAccountValue(trader.getAccountBalance() + trader.getPortfolioValue());
            trader.setUtilizationPercentage(trader.getAccountBalance() / (trader.getAccountBalance() + trader.getPortfolioValue()));

            //
            this.traderService.saveTraderAccount(trader);

            //        System.out.println("trader / after / " + trader);
        }

    }

    public List<AccountHistory> getHistoryByTraderName(String traderName) {

        List<AccountHistory> accountHistoryList = this.traderService.getAccountByName(traderName).getAccountHistory();

        SortHistory.sortAccountHistoryByDate(accountHistoryList);

        return accountHistoryList;

    }

}