package digital.softwareshinobi.napkinexchange.market.configuration;

import digital.softwareshinobi.napkinexchange.market.model.Market;
import digital.softwareshinobi.napkinexchange.market.service.MarketService;
import digital.softwareshinobi.napkinexchange.notification.model.Notification;
import digital.softwareshinobi.napkinexchange.notification.service.NotificationService;
import digital.softwareshinobi.napkinexchange.notification.type.NotificationType;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
@AllArgsConstructor
public class MarketConfiguration {

    private final Logger logger = LoggerFactory.getLogger(MarketConfiguration.class);

    @Autowired
    private final MarketService marketService;

    @Autowired
    private final NotificationService notificationService;

    @PostConstruct
    public void initializeMarket() {

        //Calling this method will automatically create a new Market Entity if one doesn't exist
        //This happens because only a single market entity should exist, with ID 1
        Market market = this.marketService.getMarket();

        this.notificationService.save(new Notification(
                "SYSTEM",
                NotificationType.SYSTEM_NOTIFICATION,
                "napkin exchange market simulation initialized"
        ));

        logger.info("current market conditions: " + market.toString());

    }

}
