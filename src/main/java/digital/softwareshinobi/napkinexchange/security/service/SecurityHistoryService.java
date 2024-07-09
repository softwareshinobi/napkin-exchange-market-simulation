package digital.softwareshinobi.napkinexchange.security.service;

import digital.softwareshinobi.napkinexchange.market.model.Market;
import digital.softwareshinobi.napkinexchange.market.service.MarketService;
import digital.softwareshinobi.napkinexchange.notification.controller.NotificationController;
import digital.softwareshinobi.napkinexchange.security.model.Security;
import digital.softwareshinobi.napkinexchange.security.model.SecurityPricingHistory;
import digital.softwareshinobi.napkinexchange.security.model.SecurityPricingHistoryId;
import digital.softwareshinobi.napkinexchange.security.repository.SecurityPricingHistoryRepository;
import digital.softwareshinobi.napkinexchange.security.utility.SortHistory;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class SecurityHistoryService {

    private final org.slf4j.Logger logger = LoggerFactory.getLogger(SecurityHistoryService.class);

    @Autowired
    private final SecurityPricingHistoryRepository securityPricingHistoryRepository;

    @Autowired
    private final SecurityService securityService;

    @Autowired
    private final MarketService marketService;

    private static final int DEFAULT_MAX_LIST_SIZE = 100;

    public void updateSecurityHistory() {

        Market market = this.marketService.getMarket();

        for (Security currentSecurity : this.securityService.getAllSecurities()) {

//logger.debug("currentSecurity / " + currentSecurity);
//
            Optional<SecurityPricingHistory> previousSecurityPricingHistoryOptional
                    = securityPricingHistoryRepository.findTopBySecurityOrderByIdDesc(currentSecurity);

            double previousSecurityPricing = -1d;
            double gainPercent = -2d;
            double gainValue = -3d;

            if (previousSecurityPricingHistoryOptional.isPresent()) {

                //     logger.debug("previousSecurityPricingHistoryOptional / " + previousSecurityPricingHistoryOptional);
//
                SecurityPricingHistory previousSecurityPricingHistory = previousSecurityPricingHistoryOptional.get();

                //   logger.debug("previousSecurityPricingHistory / " + previousSecurityPricingHistory);
//
                previousSecurityPricing = previousSecurityPricingHistory.getPrice();

                //  logger.debug("previousSecurityPricing / " + previousSecurityPricing);
                Double currentSecurityPricing = currentSecurity.getPrice();

                //   logger.debug("currentSecurityPricing / " + currentSecurityPricing);
                gainValue = currentSecurityPricing - previousSecurityPricing;

                //  logger.debug("gainValue / " + gainValue);
                if (previousSecurityPricing != 0) { // Avoid division by zero
                    gainPercent = (gainValue / previousSecurityPricing); //* 100;
                    //       logger.debug("gainPercent / " + gainPercent);
                }
                //   gain

                // Calculate gain/loss in percentage
                // Print the results
                //   logger.debug("Gain/Loss in Value: " + gainValue);
                //   logger.debug("Gain/Loss in Percentage: " + gainPercent + "%");
            } else {

                //     logger.debug("nonnneeee // No recent pricing history found for the company");
            }

            SecurityPricingHistory newSecurityPricingHistory = new SecurityPricingHistory(
                    new SecurityPricingHistoryId(
                            market.getDate(),
                            currentSecurity.getTicker()),
                    currentSecurity,
                    currentSecurity.getPrice(), previousSecurityPricing,
                    gainPercent,
                    gainValue
            );

            //      logger.debug("newSecurityPricingHistory / " + newSecurityPricingHistory);
            this.securityPricingHistoryRepository.save(newSecurityPricingHistory);

        }

    }

    public List<SecurityPricingHistory> getSecurityPricingHistoryByTicker(String symbol) {

        List<SecurityPricingHistory> securityPricingHistoryList = this.securityPricingHistoryRepository
                .findAll()
                .stream()
                .filter(securityPricingHistory
                        -> securityPricingHistory.getSecurity().getTicker().equalsIgnoreCase(symbol))
                //    .limit(DEFAULT_MAX_LIST_SIZE)
                .collect(Collectors.toList());

        while (securityPricingHistoryList.size() > DEFAULT_MAX_LIST_SIZE) {

            securityPricingHistoryList.remove(0);

            // logger.debug("size: "+securityPricingHistoryList.size());
        }

        //  SortHistory.sortSecurityHistoryListByDate(securityPricingHistoryList);
        return securityPricingHistoryList;

    }

}
