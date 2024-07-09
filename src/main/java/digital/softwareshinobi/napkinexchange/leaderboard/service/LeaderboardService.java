package digital.softwareshinobi.napkinexchange.leaderboard.service;

import digital.softwareshinobi.napkinexchange.broker.service.SecurityPortfolioService;
import digital.softwareshinobi.napkinexchange.leaderboard.model.Leaderboard;
import digital.softwareshinobi.napkinexchange.leaderboard.utility.TraderSortingUtility;
import digital.softwareshinobi.napkinexchange.trader.model.Trader;
import digital.softwareshinobi.napkinexchange.trader.service.TraderService;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class LeaderboardService {

    private final org.slf4j.Logger logger = LoggerFactory.getLogger(LeaderboardService.class);

    @Autowired
    private final TraderService traderService;

    public List<Leaderboard> topTenTraders() {

        List<Trader> traderList
                = TraderSortingUtility.sortTraderByAccountValue(
                        this.traderService.findAllAccounts());

        logger.debug("trader list:");

        logger.debug(traderList);

        return traderList.stream()
                .map(trader
                        -> new Leaderboard(
                        traderList.indexOf(trader) + 1,
                        trader.getUsername(),
                        trader.getAccountBalance(),
                        trader.getAccountValue()))
                .limit(10)
                .collect(Collectors.toList());

    }

    public Leaderboard findByTraderName(String traderName) {

        List<Trader> traderList
                = TraderSortingUtility.sortTraderByAccountValue(
                        this.traderService.findAllAccounts());

        return traderList.stream()
                .filter(trader -> trader.getUsername().equals(traderName))
                .map(trader -> new Leaderboard(
                traderList.indexOf(trader) + 1,
                trader.getUsername(),
                trader.getAccountBalance(),
                trader.getAccountValue()))
                .findFirst().orElse(null);

    }

}

/*

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

 */
