package digital.softwareshinobi.napkinexchange.trader.service;

import digital.softwareshinobi.napkinexchange.notification.model.Notification;
import digital.softwareshinobi.napkinexchange.notification.service.NotificationService;
import digital.softwareshinobi.napkinexchange.notification.type.NotificationType;
import digital.softwareshinobi.napkinexchange.trader.exception.TraderNotFoundException;
import digital.softwareshinobi.napkinexchange.trader.exception.InvalidAccountException;
import digital.softwareshinobi.napkinexchange.trader.model.Trader;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import digital.softwareshinobi.napkinexchange.trader.repository.TraderRepository;

@Service
@AllArgsConstructor
public class TraderService {

    @Autowired
    private final TraderRepository traderRepository;

    @Autowired
    private final NotificationService notificationService;

    private final Double DEFAULT_NEW_TRADER_BALANCE = 1_000_000.00;

    public List<Trader> findAllAccounts() {

        return this.traderRepository.findAll();

    }

    public Trader getAccountByName(String username) throws TraderNotFoundException {

        return this.traderRepository.findById(username)
                .orElseThrow(() -> new TraderNotFoundException("No account with that username"));

    }

    public void saveTraderAccount(Trader trader) {

System.out.println("saving trader account");

this.traderRepository.save(trader);

System.out.println("saved trader account");
    }

    public Trader createTraderAccount(String traderName, String traderPassword) throws InvalidAccountException {

        if (doesTraderAccountExist(traderName)) {

            throw new InvalidAccountException("Unable to create Trader. There is already an Trader with that username");

        }

        Trader trader = new Trader(traderName, traderPassword);

        trader.setAvailableFunds(DEFAULT_NEW_TRADER_BALANCE);

        Trader savedTrader = this. traderRepository.save(trader);

        notificationService.save(new Notification(
                savedTrader,
                NotificationType.NEW_TRADER_CREATED,
                savedTrader.toString()
        ));

        trader = null;

        return savedTrader;

    }

    public boolean doesTraderAccountExist(String trader) {

        try {

            getAccountByName(trader);

            return true;

        } catch (TraderNotFoundException exception) {

            return false;

        }

    }

//    public void updateBalanceAndSave(AccountTransaction accountTransaction)
//            throws TraderNotFoundException, TraderBalanceException {
//
//        Trader account = getAccountByName(accountTransaction.getUsername());
//
//        account.updateAccountBalance(accountTransaction.getAmountToAdd());
//
//        this.saveTraderAccount(account);
//
//        account = null;
//
//    }
//    public void updateBalanceAndSave(Trader trader, double amountToAdd) {
//
//        trader.updateAccountBalance(amountToAdd);
//
//        this.saveTraderAccount(trader);
//
//    }

}
