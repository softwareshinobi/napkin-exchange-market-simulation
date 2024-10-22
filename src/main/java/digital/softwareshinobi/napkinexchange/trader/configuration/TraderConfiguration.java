package digital.softwareshinobi.napkinexchange.trader.configuration;

import digital.softwareshinobi.napkinexchange.trader.exception.InvalidAccountException;
import digital.softwareshinobi.napkinexchange.trader.service.TraderService;
import jakarta.annotation.PostConstruct;
import java.util.logging.Level;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TraderConfiguration {

    private final Logger logger = LoggerFactory.getLogger(TraderConfiguration.class);

    private final String DEMO_USER = "projectchimba";

    private final String DEMO_PASS = "whiplashone";

    @Autowired
    private TraderService accountService;

    @PostConstruct
    public void createMateoDevelopmentUser() {

        if (!accountService.doesTraderAccountExist(DEMO_USER)) {

            logger.info("creating system user / [{}]", DEMO_USER);

            try {

                this.accountService.createTraderAccount(DEMO_USER, DEMO_PASS);

            } catch (InvalidAccountException ex) {

                java.util.logging.Logger.getLogger(TraderService.class.getName()).log(Level.SEVERE, null, ex);

            }

        }

    }

}
