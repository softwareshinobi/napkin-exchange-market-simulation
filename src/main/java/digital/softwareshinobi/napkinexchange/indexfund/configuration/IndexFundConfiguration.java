package digital.softwareshinobi.napkinexchange.indexfund.configuration;

import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import digital.softwareshinobi.napkinexchange.indexfund.defaults.DefaultIndexFunds;
import digital.softwareshinobi.napkinexchange.indexfund.service.IndexFundService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
@AllArgsConstructor
public class IndexFundConfiguration {

    @Autowired
    private final IndexFundService indexFundService;

    @PostConstruct
    public void saveDefaultIndexFundsOnStartup() {
        if (DefaultIndexFunds.getAmountOfIndexFunds() != indexFundService.findIndexFundRowCount()) {
            indexFundService.updatePriceForAllFundsDaily();
        }
    }
}
