package digital.softwareshinobi.napkinexchange.account.service;

import lombok.AllArgsConstructor;
import digital.softwareshinobi.napkinexchange.market.entity.Market;
import digital.softwareshinobi.napkinexchange.market.service.MarketService;
import digital.softwareshinobi.napkinexchange.stocks.stock.utils.SortHistory;
import digital.softwareshinobi.napkinexchange.account.model.entity.AccountHistory;
import digital.softwareshinobi.napkinexchange.account.repository.AccountHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
        Market market = marketService.findMarketEntity();
        accountService.findAllAccounts().forEach(account -> accountHistoryRepository.save(
                new AccountHistory(market.getDate(), account, account.getTotalProfits()
                )));
    }

    public List<AccountHistory> findHistoryOfAccount(String username) {
        List<AccountHistory> accountHistory
                = accountService.getAccountByName(username).getAccountHistory();
        SortHistory.sortAccountHistoryByDate(accountHistory);
        return accountHistory;
    }

    @Transactional
    public void truncateAccountHistoryAtEndOfYear() {
        accountHistoryRepository.truncateTable();
    }
}
