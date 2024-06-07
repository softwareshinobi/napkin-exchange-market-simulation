package digital.softwareshinobi.napkinexchange.leaderboard.utility;

import digital.softwareshinobi.napkinexchange.trader.model.Trader;
import java.util.Comparator;
import java.util.List;

public class TraderSortingUtility {

    public static List<Trader> sortTraderByAccountValue(List<Trader> traderList) {

        traderList.sort(new Comparator<Trader>() {

            @Override
            public int compare(Trader traderOne, Trader traderTwo) {

                return traderTwo.getAccountValue().compareTo(traderOne.getAccountValue());

            }

        });

        return traderList;

    }

}
