package digital.softwareshinobi.napkinexchange.trader.service;

import lombok.AllArgsConstructor;
import digital.softwareshinobi.napkinexchange.trader.exception.AccountBalanceException;
import digital.softwareshinobi.napkinexchange.trader.exception.AccountNotFoundException;
import digital.softwareshinobi.napkinexchange.trader.exception.InvalidAccountException;
import digital.softwareshinobi.napkinexchange.trader.model.entity.Account;
import digital.softwareshinobi.napkinexchange.trader.model.payload.AccountTransaction;
import digital.softwareshinobi.napkinexchange.trader.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class AccountService {

    @Autowired
    private final AccountRepository accountRepository;

    public List<Account> findAllAccounts() {
        return accountRepository.findAll();
    }

    public Account getAccountByName(String username) throws AccountNotFoundException {
        return accountRepository.findById(username)
                .orElseThrow(() -> new AccountNotFoundException("No account with that username"));
    }

    public void saveAccount(Account account) {
        accountRepository.save(account);
    }

    public void createNewAccount(String username) throws InvalidAccountException {
        if (accountExists(username)) {
            throw new InvalidAccountException("Account already exists with that username");
        }
        accountRepository.save(new Account(username));
    }

    public boolean accountExists(String username) {
        try {
            getAccountByName(username);
            return true;
        } catch (AccountNotFoundException ex) {
            return false;
        }
    }

    public void updateBalanceAndSave(AccountTransaction accountTransaction)
            throws AccountNotFoundException, AccountBalanceException {
        Account account = getAccountByName(accountTransaction.getUsername());
        account.updateAccountBalance(accountTransaction.getAmountToAdd());
        saveAccount(account);
    }

    public void updateBalanceAndSave(Account account, double amountToAdd) {
        account.updateAccountBalance(amountToAdd);
        saveAccount(account);
    }
}
