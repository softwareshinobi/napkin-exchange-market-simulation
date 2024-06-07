package digital.softwareshinobi.napkinexchange.leaderboard.service;

import digital.softwareshinobi.napkinexchange.leaderboard.model.Leaderboard;
import digital.softwareshinobi.napkinexchange.leaderboard.utility.TraderSortingUtility;
import digital.softwareshinobi.napkinexchange.trader.model.Trader;
import digital.softwareshinobi.napkinexchange.trader.service.TraderService;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class LeaderboardService {

    @Autowired
    private final TraderService traderService;

    public List<Leaderboard> topThreeTraders() {

        List<Trader> traderList
                = TraderSortingUtility.sortTraderByAccountValue(
                         this.traderService.findAllAccounts());

        return traderList.stream()
                .map(trader -> new Leaderboard(
                traderList.indexOf(trader) + 1,
                trader.getUsername(),
                trader.getAccountBalance(),
                trader.getTotalProfits()))
                .limit(3)
                .collect(Collectors.toList());

    }

    public List<Leaderboard> topTenTraders() {

        List<Trader> traderList
                = TraderSortingUtility.sortTraderByAccountValue(
                         this.traderService.findAllAccounts());

        return traderList.stream()
                .map(trader
                        -> new Leaderboard(
                        traderList.indexOf(trader) + 1,
                        trader.getUsername(),
                        trader.getAccountBalance(),
                        trader.getTotalProfits()))
                .limit(10)
                .collect(Collectors.toList());

    }

    public Leaderboard findByTraderName(String traderName) {

        List<Trader> traderList
                = TraderSortingUtility.sortTraderByAccountValue(
                        this.traderService.findAllAccounts());

        return traderList.stream()
                .filter(account -> account.getUsername().equals(traderName))
                .map(account -> new Leaderboard(
                traderList.indexOf(account) + 1,
                account.getUsername(),
                account.getAccountBalance(),
                account.getTotalProfits()))
                .findFirst().orElse(null);

    }

}
