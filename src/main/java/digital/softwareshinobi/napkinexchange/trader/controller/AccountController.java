package digital.softwareshinobi.napkinexchange.trader.controller;

import lombok.AllArgsConstructor;
import digital.softwareshinobi.napkinexchange.trader.exception.AccountBalanceException;
import digital.softwareshinobi.napkinexchange.trader.exception.AccountNotFoundException;
import digital.softwareshinobi.napkinexchange.trader.exception.InvalidAccountException;
import digital.softwareshinobi.napkinexchange.trader.model.entity.Account;
import digital.softwareshinobi.napkinexchange.trader.model.entity.AccountHistory;
import digital.softwareshinobi.napkinexchange.trader.model.payload.AccountTransaction;
import digital.softwareshinobi.napkinexchange.trader.service.AccountHistoryService;
import digital.softwareshinobi.napkinexchange.trader.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import org.json.JSONObject;

@RestController
@RequestMapping(value = "/account")
@AllArgsConstructor
@CrossOrigin
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

 @PostMapping(value = "/create")
    public void calculateTextPercentageDifference(@RequestBody String userContentToBeRewritter) throws InvalidAccountException {
          
        System.out.println("the user string: ");
        
        System.out.println(userContentToBeRewritter);
        
        JSONObject ox = new JSONObject(userContentToBeRewritter);
        
        System.out.println("ox ox ox: ");
        
        System.out.println(ox);
        
        String username = ox.getString("username");
                
        System.out.println("the user username: ");
    
        System.out.println(username);

        if (username.equalsIgnoreCase("trading-bot")) {
     
            throw new InvalidAccountException("Cannot Create Account With Invalid Username");
   
        }
         
        accountService.createNewAccount(username);
 
    }

    @PostMapping(value = "/deposit")
    public void depositToAccount(@RequestBody AccountTransaction accountTransaction) throws AccountNotFoundException, AccountBalanceException {
  
        accountService.updateBalanceAndSave(accountTransaction);
  
    }

    @GetMapping(value = "/history/{username}")
    public List<AccountHistory> getAccountHistory(@PathVariable String username) throws AccountNotFoundException, AccountBalanceException {
  
        return accountHistoryService.findHistoryOfAccount(username);
  
    }
}
