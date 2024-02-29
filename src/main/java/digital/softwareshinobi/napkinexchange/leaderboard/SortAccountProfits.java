package digital.softwareshinobi.napkinexchange.leaderboard;

import digital.softwareshinobi.napkinexchange.trader.model.Account;
import java.util.Comparator;
import java.util.List;

class SortAccountProfits {

    public static List<Account> sortAccountByProfits(List<Account> accounts) {

        accounts.sort(new Comparator<Account>() {

            @Override
            public int compare(Account account1, Account account2) {

                return account2.getTotalProfits().compareTo(account1.getTotalProfits());

            }

        });

        return accounts;

    }

}
