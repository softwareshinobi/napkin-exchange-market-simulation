package digital.softwareshinobi.napkinexchange.trader.service;

import digital.softwareshinobi.napkinexchange.market.model.Market;
import digital.softwareshinobi.napkinexchange.market.service.MarketService;
import digital.softwareshinobi.napkinexchange.security.utils.SortHistory;
import digital.softwareshinobi.napkinexchange.trader.model.AccountHistory;
import digital.softwareshinobi.napkinexchange.trader.repository.AccountHistoryRepository;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AccountHistoryService {

    @Autowired
    private final AccountHistoryRepository accountHistoryRepository;

    @Autowired
    private final AccountService accountService;

    @Autowired
    private final MarketService marketService;

    public void saveDailyAccountHistory() {

        Market market = this.marketService.getMarket();

        this.accountService.findAllAccounts().forEach(
                account -> this.accountHistoryRepository.save(
                        new AccountHistory(
                                market.getDate(),
                                account,
                                account.getTotalProfits()
                        )
                ));

    }

    public List<AccountHistory> getHistoryByUsername(String username) {

        List<AccountHistory> accountHistoryList = this.accountService.getAccountByName(username).getAccountHistory();

        SortHistory.sortAccountHistoryByDate(accountHistoryList);

        return accountHistoryList;

    }

//    @Transactional
//    public void truncateAccountHistoryAtEndOfYear() {
//
//        accountHistoryRepository.truncateTable();
//
//    }
}
