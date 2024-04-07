package digital.softwareshinobi.napkinexchange.trader.service;

import digital.softwareshinobi.napkinexchange.broker.types.AccountTransaction;
import digital.softwareshinobi.napkinexchange.notification.model.Notification;
import digital.softwareshinobi.napkinexchange.notification.model.NotificationType;
import digital.softwareshinobi.napkinexchange.notification.service.NotificationService;
import digital.softwareshinobi.napkinexchange.trader.exception.AccountBalanceException;
import digital.softwareshinobi.napkinexchange.trader.exception.AccountNotFoundException;
import digital.softwareshinobi.napkinexchange.trader.exception.InvalidAccountException;
import digital.softwareshinobi.napkinexchange.trader.model.Trader;
import digital.softwareshinobi.napkinexchange.trader.repository.AccountRepository;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AccountService {

    @Autowired
    private final AccountRepository accountRepository;

    @Autowired
    private final NotificationService notificationService;

    private final Integer DEFAULTBALANCE = 1000000;

    public List<Trader> findAllAccounts() {

        return accountRepository.findAll();

    }

    public Trader getAccountByName(String username) throws AccountNotFoundException {

        return accountRepository.findById(username)
                .orElseThrow(() -> new AccountNotFoundException("No account with that username"));

    }

    public void saveTraderAccount(Trader account) {

        accountRepository.save(account);

    }

    public Trader createTraderAccount(String username, String password) throws InvalidAccountException {

        if (doesTraderAccountExist(username)) {

            throw new InvalidAccountException("Unable to create account. There is already an account with that username");

        }

        Trader newAccount = new Trader(username, password);

        newAccount.updateAccountBalance(DEFAULTBALANCE);

        Trader resultAccount = accountRepository.save(newAccount);

        notificationService.save(
                new Notification(
                        resultAccount,
                        NotificationType.NEW_TRADER_CREATED,
                        resultAccount.toString()
                ));

        newAccount = null;

        return resultAccount;

    }

    public boolean doesTraderAccountExist(String username) {

        try {

            getAccountByName(username);

            return true;

        } catch (AccountNotFoundException exception) {

            return false;

        }

    }

    public void updateBalanceAndSave(AccountTransaction accountTransaction)
            throws AccountNotFoundException, AccountBalanceException {

        Trader account = getAccountByName(accountTransaction.getUsername());

        account.updateAccountBalance(accountTransaction.getAmountToAdd());

        this.saveTraderAccount(account);

        account = null;

    }

    public void updateBalanceAndSave(Trader account, double amountToAdd) {

        account.updateAccountBalance(amountToAdd);

        this.saveTraderAccount(account);

    }

}
