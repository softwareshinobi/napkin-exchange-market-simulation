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
    private final TraderService traderService;

    @Autowired
    private final TraderHistoryService accountHistoryService;

    @GetMapping(value = "")
    public List<Trader> getAllTraders() throws TraderNotFoundException {

        return this.traderService.findAllAccounts();

    }

    @PostMapping(value = "")
    public void createTrader(@RequestBody Map traderDetails) throws InvalidAccountException {

        System.out.println("enter > createTrader");

        System.out.println("traderDetails / " + traderDetails);

        String username = (String) traderDetails.get("username");

        String password = (String) traderDetails.get("password");

        System.out.println("new trader / " + username);

        this.traderService.createTraderAccount(username, password);

    }

    @RequestMapping(value = "{traderName}")
    public Trader getTraderByName(@PathVariable String traderName) throws TraderNotFoundException {

        return this.traderService.getAccountByName(traderName);

    }

    @GetMapping(value = "/history/{username}")
    public List<AccountHistory> getTraderAccountHistory(@PathVariable String username) throws TraderNotFoundException, TraderBalanceException {

        return this.accountHistoryService.getHistoryByTraderName(username);

    }

}

//    @PostMapping(value = "/deposit")
//    public void depositToAccount(@RequestBody AccountTransaction accountTransaction) throws AccountNotFoundException, AccountBalanceException {
//
//        accountService.updateBalanceAndSave(accountTransaction);
//
//    }
