package digital.softwareshinobi.napkinexchange.leaderboard.service;

import digital.softwareshinobi.napkinexchange.leaderboard.model.Leaderboard;
import digital.softwareshinobi.napkinexchange.leaderboard.utility.SortAccountProfits;
import digital.softwareshinobi.napkinexchange.trader.model.Trader;
import digital.softwareshinobi.napkinexchange.trader.service.AccountService;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class LeaderboardService {

    @Autowired
    private final AccountService accountService;

    public List<Leaderboard> topTenAccounts() {

        List<Trader> accounts
                = SortAccountProfits.sortAccountByProfits(accountService.findAllAccounts());

        return accounts.stream()
                .map(account
                        -> new Leaderboard(
                        accounts.indexOf(account) + 1,
                        account.getUsername(),
                        account.getAccountBalance(),
                        account.getTotalProfits()))
                .limit(10)
                .collect(Collectors.toList());

    }

    public List<Leaderboard> topThreeAccounts() {

        List<Trader> accounts
                = SortAccountProfits.sortAccountByProfits(accountService.findAllAccounts());

        return accounts.stream()
                .map(account -> new Leaderboard(
                accounts.indexOf(account) + 1,
                account.getUsername(),
                account.getAccountBalance(),
                account.getTotalProfits()))
                .limit(3)
                .collect(Collectors.toList());

    }

    public Leaderboard findAccountRanking(String username) {

        List<Trader> accounts
                = SortAccountProfits.sortAccountByProfits(accountService.findAllAccounts());

        return accounts.stream()
                .filter(account -> account.getUsername().equals(username))
                .map(account -> new Leaderboard(
                accounts.indexOf(account) + 1,
                account.getUsername(),
                account.getAccountBalance(),
                account.getTotalProfits()))
                .findFirst().orElse(null);

    }

}
