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
public class SecurityPricingHistoryService {

    @Autowired
    private final SecurityPricingHistoryRepository securityPricingHistoryRepository;

    @Autowired
    private final SecurityService securityService;

    @Autowired
    private final MarketService marketService;

    public void updateSecurityHistory() {

        Market market = this.marketService.getMarket();

        for (Security security : this.securityService.getAllSecurities()) {

            System.out.println("security / " + security);

            Optional<SecurityPricingHistory> mostRecentPricingHistory
                    = securityPricingHistoryRepository.findTopBySecurityOrderByIdDesc(security);

            if (mostRecentPricingHistory.isPresent()) {

                System.out.println("godddd / ");

                // Access the most recent SecurityPricingHistory object
                SecurityPricingHistory recentHistory = mostRecentPricingHistory.get();

                // Use recentHistory object
                System.out.println("recentHistory / " + recentHistory);

            } else {

                System.out.println("nonnneeee // No recent pricing history found for the company");

            }

            SecurityPricingHistory newSecurityPricingHistory = new SecurityPricingHistory(
                    new SecurityPricingHistoryId(
                            market.getDate(),
                            security.getTicker()),
                    security,
                    security.getPrice());
            System.out.println("newSecurityPricingHistory / " + newSecurityPricingHistory);

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
