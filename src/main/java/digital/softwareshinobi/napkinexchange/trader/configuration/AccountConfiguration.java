package digital.softwareshinobi.napkinexchange.trader.configuration;

import digital.softwareshinobi.napkinexchange.trader.exception.InvalidAccountException;
import digital.softwareshinobi.napkinexchange.trader.service.AccountService;
import jakarta.annotation.PostConstruct;
import java.util.logging.Level;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AccountConfiguration {

    private final Logger logger = LoggerFactory.getLogger(AccountConfiguration.class);

    @Autowired
    private AccountService accountService;

    @PostConstruct
    public void createMateoDevelopmentUser() {

        if (!accountService.doesTraderAccountExist("mateo")) {

            logger.info("creating the new mateo user");

            try {

                accountService.createTraderAccount("mateo", "6468");

            } catch (InvalidAccountException ex) {

                java.util.logging.Logger.getLogger(AccountService.class.getName()).log(Level.SEVERE, null, ex);

            }

        }

    }

}
