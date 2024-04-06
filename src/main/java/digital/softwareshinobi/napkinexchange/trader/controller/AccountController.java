package digital.softwareshinobi.napkinexchange.trader.controller;

import digital.softwareshinobi.napkinexchange.trader.exception.AccountBalanceException;
import digital.softwareshinobi.napkinexchange.trader.exception.AccountNotFoundException;
import digital.softwareshinobi.napkinexchange.trader.exception.InvalidAccountException;
import digital.softwareshinobi.napkinexchange.trader.model.Account;
import digital.softwareshinobi.napkinexchange.trader.model.AccountHistory;
import digital.softwareshinobi.napkinexchange.trader.service.AccountHistoryService;
import digital.softwareshinobi.napkinexchange.trader.service.AccountService;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@AllArgsConstructor
@RequestMapping(value = "trader")
public class AccountController {

    @Autowired
    private final AccountService accountService;

    @Autowired
    private final AccountHistoryService accountHistoryService;

    @GetMapping(value = "")
    public List<Account> listallaccounts() throws AccountNotFoundException {

        return accountService.findAllAccounts();

    }

    @RequestMapping(value = "{username}")
    public Account getAccountByUsername(@PathVariable String username) throws AccountNotFoundException {

        return accountService.getAccountByName(username);

    }

    @PostMapping(value = "create")
    public void createTrader(@RequestBody Map traderDetails) throws InvalidAccountException {

        System.out.println("enter > createTrader");

        System.out.println("traderDetails / " + traderDetails);

        String username = (String) traderDetails.get("username");

        String password = (String) traderDetails.get("password");

        System.out.println("new trader / " + username);

        accountService.createTraderAccount(username, password);

    }

    @GetMapping(value = "/history/{username}")
    public List<AccountHistory> getAccountHistory(@PathVariable String username) throws AccountNotFoundException, AccountBalanceException {

        return accountHistoryService.getHistoryByUsername(username);

    }

}

//    @PostMapping(value = "/deposit")
//    public void depositToAccount(@RequestBody AccountTransaction accountTransaction) throws AccountNotFoundException, AccountBalanceException {
//
//        accountService.updateBalanceAndSave(accountTransaction);
//
//    }
