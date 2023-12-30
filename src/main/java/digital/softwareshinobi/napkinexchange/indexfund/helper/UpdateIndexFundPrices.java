package digital.softwareshinobi.napkinexchange.indexfund.helper;

import lombok.AllArgsConstructor;
import digital.softwareshinobi.napkinexchange.indexfund.defaults.DefaultIndexFunds;
import digital.softwareshinobi.napkinexchange.stocks.stock.enums.Volatility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import digital.softwareshinobi.napkinexchange.indexfund.model.subclass.MarketCapIndexFund;
import digital.softwareshinobi.napkinexchange.indexfund.model.subclass.SectorIndexFund;
import digital.softwareshinobi.napkinexchange.indexfund.model.subclass.StableIndexFund;
import digital.softwareshinobi.napkinexchange.indexfund.model.subclass.TotalMarketIndexFund;
import digital.softwareshinobi.napkinexchange.indexfund.model.subclass.VolatilityIndexFund;

@Component
@AllArgsConstructor
public class UpdateIndexFundPrices {

    @Autowired
    private final CalculateIndexFundPrice calculateIndexFundPrice;

    public List<MarketCapIndexFund> updateMarketCapIndexFunds() {
        List<MarketCapIndexFund> funds = DefaultIndexFunds.marketCapIndexFunds;
        funds.forEach(fund -> fund.setPrice(
                calculateIndexFundPrice.findPriceOfMarketCapFund(fund.getMarketCap())));
        return funds;
    }

    public List<SectorIndexFund> updateSectorIndexFunds() {
        List<SectorIndexFund> funds = DefaultIndexFunds.sectorIndexFunds;
        funds.forEach(fund -> fund.setPrice(
                calculateIndexFundPrice.findPriceOfSectorFund(fund.getSector())));
        return funds;
    }

    public VolatilityIndexFund updateVolatileIndexFund() {
        VolatilityIndexFund fund = DefaultIndexFunds.volatilityIndexFund;
        fund.setPrice(calculateIndexFundPrice.findPriceOfVolatileFunds(Volatility.VOLATILE));
        return fund;
    }

    public StableIndexFund updateStableIndexFund() {
        StableIndexFund fund = DefaultIndexFunds.stableIndexFund;
        fund.setPrice(calculateIndexFundPrice.findPriceOfVolatileFunds(Volatility.STABLE));
        return fund;
    }

    public TotalMarketIndexFund updateTotalMarketIndexFund() {
        TotalMarketIndexFund fund = DefaultIndexFunds.totalMarketIndexFund;
        fund.setPrice(calculateIndexFundPrice.findPriceOfTotalMarketFund());
        return fund;
    }
}
