package digital.softwareshinobi.napkinexchange.security.configuration;

import digital.softwareshinobi.napkinexchange.security.defaults.DefaultStocksList;
import digital.softwareshinobi.napkinexchange.security.service.SecurityService;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
@AllArgsConstructor
public class SecurityConfiguration {

    private final Logger logger = LoggerFactory.getLogger(SecurityConfiguration.class);

    @Autowired
    private SecurityService securityService;

    @PostConstruct
    public void saveSecuritiesToDatabase() {
        if (DefaultStocksList.getCountForDefaultStocks() != securityService.findStockRowCount()) {

            logger.info("Saving Default Securities");

            this.securityService.saveDefaultStockToDatabase(DefaultStocksList.getAllDefaultStocks());

        }
    }
}
