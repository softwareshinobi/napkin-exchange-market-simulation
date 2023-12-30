package digital.softwareshinobi.napkinexchange.indexfund.service;

import lombok.AllArgsConstructor;
import digital.softwareshinobi.napkinexchange.indexfund.enums.FundTracking;
import digital.softwareshinobi.napkinexchange.indexfund.helper.UpdateIndexFundPrices;
import digital.softwareshinobi.napkinexchange.indexfund.model.IndexFund;
import digital.softwareshinobi.napkinexchange.indexfund.repository.IndexFundRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class IndexFundService {

    @Autowired
    private final IndexFundRepository indexFundRepository;
    @Autowired
    private final UpdateIndexFundPrices updateIndexFundPrices;

    public void updatePriceForAllFundsDaily() {
        updateIndexFundPrices.updateMarketCapIndexFunds().forEach(indexFundRepository::save);
        updateIndexFundPrices.updateSectorIndexFunds().forEach(indexFundRepository::save);
        indexFundRepository.save(updateIndexFundPrices.updateStableIndexFund());
        indexFundRepository.save(updateIndexFundPrices.updateVolatileIndexFund());
        indexFundRepository.save(updateIndexFundPrices.updateTotalMarketIndexFund());
    }

    public List<IndexFund> findAllIndexFunds() {
        return indexFundRepository.findAll();
    }

    public List<IndexFund> findIndexFundByTracker(FundTracking fundTracking) {
        return indexFundRepository.findAll().stream()
                .filter(fund -> fund.getFundTracking() == fundTracking)
                .collect(Collectors.toList());
    }

    public long findIndexFundRowCount() {
        return indexFundRepository.count();
    }
}
