package digital.softwareshinobi.napkinexchange.trader.controller;

import digital.softwareshinobi.napkinexchange.trader.exception.TraderBalanceException;
import digital.softwareshinobi.napkinexchange.trader.exception.TraderNotFoundException;
import digital.softwareshinobi.napkinexchange.trader.exception.InvalidAccountException;
import digital.softwareshinobi.napkinexchange.trader.model.Trader;
import digital.softwareshinobi.napkinexchange.trader.model.AccountHistory;
import digital.softwareshinobi.napkinexchange.trader.service.TraderHistoryService;
import digital.softwareshinobi.napkinexchange.trader.service.TraderService;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@AllArgsConstructor
@RequestMapping(value = "trader")
public class TraderController {

    @Autowired
    private final TraderService accountService;

    @Autowired
    private final TraderHistoryService accountHistoryService;

    @GetMapping(value = "")
    public List<Trader> listallaccounts() throws TraderNotFoundException {

        return accountService.findAllAccounts();

    }

    @RequestMapping(value = "{traderName}")
    public Trader getAccountByUsername(@PathVariable String traderName) throws TraderNotFoundException {

        return this.accountService.getAccountByName(traderName);

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
    public List<AccountHistory> getAccountHistory(@PathVariable String username) throws TraderNotFoundException, TraderBalanceException {

        return accountHistoryService.getHistoryByTraderName(username);

    }

}

//    @PostMapping(value = "/deposit")
//    public void depositToAccount(@RequestBody AccountTransaction accountTransaction) throws AccountNotFoundException, AccountBalanceException {
//
//        accountService.updateBalanceAndSave(accountTransaction);
//
//    }
