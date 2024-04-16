package digital.softwareshinobi.napkinexchange.leaderboard.utility;

import digital.softwareshinobi.napkinexchange.trader.model.Trader;
import java.util.Comparator;
import java.util.List;

public class SortAccountProfits {

    public static List<Trader> sortAccountByProfits(List<Trader> accounts) {

        accounts.sort(new Comparator<Trader>() {

            @Override
            public int compare(Trader account1, Trader account2) {

                return account2.getTotalProfits().compareTo(account1.getTotalProfits());

            }

        });

        return accounts;

    }

}
