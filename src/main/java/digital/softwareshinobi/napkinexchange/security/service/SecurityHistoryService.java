package digital.softwareshinobi.napkinexchange.security.service;

import digital.softwareshinobi.napkinexchange.market.model.Market;
import digital.softwareshinobi.napkinexchange.market.service.MarketService;
import digital.softwareshinobi.napkinexchange.security.model.Security;
import digital.softwareshinobi.napkinexchange.security.model.SecurityPricingHistory;
import digital.softwareshinobi.napkinexchange.security.model.SecurityPricingHistoryId;
import digital.softwareshinobi.napkinexchange.security.repository.SecurityPricingHistoryRepository;
import digital.softwareshinobi.napkinexchange.security.utility.SortHistory;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class SecurityHistoryService {

    @Autowired
    private final SecurityPricingHistoryRepository securityPricingHistoryRepository;

    @Autowired
    private final SecurityService securityService;

    @Autowired
    private final MarketService marketService;

    public void updateSecurityHistory() {

        Market market = this.marketService.getMarket();

        for (Security currentSecurity : this.securityService.getAllSecurities()) {

//System.out.println("currentSecurity / " + currentSecurity);
//
            Optional<SecurityPricingHistory> previousSecurityPricingHistoryOptional
                    = securityPricingHistoryRepository.findTopBySecurityOrderByIdDesc(currentSecurity);

            double previousSecurityPricing = -1d;
            double gainPercent = -2d;
            double gainValue = -3d;

            if (previousSecurityPricingHistoryOptional.isPresent()) {

           //     System.out.println("previousSecurityPricingHistoryOptional / " + previousSecurityPricingHistoryOptional);
//
                SecurityPricingHistory previousSecurityPricingHistory = previousSecurityPricingHistoryOptional.get();

             //   System.out.println("previousSecurityPricingHistory / " + previousSecurityPricingHistory);
//
                previousSecurityPricing = previousSecurityPricingHistory.getPrice();

              //  System.out.println("previousSecurityPricing / " + previousSecurityPricing);

                Double currentSecurityPricing = currentSecurity.getPrice();

             //   System.out.println("currentSecurityPricing / " + currentSecurityPricing);

                gainValue = currentSecurityPricing - previousSecurityPricing;

              //  System.out.println("gainValue / " + gainValue);

                if (previousSecurityPricing != 0) { // Avoid division by zero
                    gainPercent = (gainValue / previousSecurityPricing); //* 100;
             //       System.out.println("gainPercent / " + gainPercent);
                }
                //   gain

                // Calculate gain/loss in percentage
                // Print the results
             //   System.out.println("Gain/Loss in Value: " + gainValue);
             //   System.out.println("Gain/Loss in Percentage: " + gainPercent + "%");

            } else {

           //     System.out.println("nonnneeee // No recent pricing history found for the company");

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

      //      System.out.println("newSecurityPricingHistory / " + newSecurityPricingHistory);

            this.securityPricingHistoryRepository.save(newSecurityPricingHistory);

        }

    }

    public List<SecurityPricingHistory> getSecurityPricingHistoryBySymbol(String symbol) {

        List<SecurityPricingHistory> securityPricingHistoryList = securityPricingHistoryRepository
                .findAll().stream()
                .filter(
                        history -> history.getSecurity().getTicker().equalsIgnoreCase(symbol))
                .collect(Collectors.toList());

        SortHistory.sortStockHistoryByDate(securityPricingHistoryList);

        return securityPricingHistoryList;

    }

}
