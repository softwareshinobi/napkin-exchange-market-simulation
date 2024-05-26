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
public class TraderConfiguration {

    private final Logger logger = LoggerFactory.getLogger(TraderConfiguration.class);

    private final String DEMO_USER = "demo";

    private final String DEMO_PASS = "whiplashone";

    @Autowired
    private AccountService accountService;

    @PostConstruct
    public void createMateoDevelopmentUser() {


        if (!accountService.doesTraderAccountExist(DEMO_USER)) {

            logger.info("creating the new {} user", DEMO_USER);

            try {

                accountService.createTraderAccount(DEMO_USER, DEMO_PASS);

            } catch (InvalidAccountException ex) {

                java.util.logging.Logger.getLogger(AccountService.class.getName()).log(Level.SEVERE, null, ex);

            }

        }

    }

}
