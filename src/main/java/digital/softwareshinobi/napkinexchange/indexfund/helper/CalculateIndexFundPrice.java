package digital.softwareshinobi.napkinexchange.indexfund.helper;

import lombok.AllArgsConstructor;
import digital.softwareshinobi.napkinexchange.market.utils.MarketTrajectoryUtils;
import digital.softwareshinobi.napkinexchange.stocks.stock.enums.MarketCap;
import digital.softwareshinobi.napkinexchange.stocks.stock.enums.Volatility;
import digital.softwareshinobi.napkinexchange.stocks.stock.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class CalculateIndexFundPrice {

    @Autowired
    private StockService stockService;

    public double findPriceOfTotalMarketFund() {
        return MarketTrajectoryUtils.stockPricesAverage(stockService.getAllStocks());
    }

    public double findPriceOfMarketCapFund(MarketCap marketCap) {
        return MarketTrajectoryUtils.stockPricesAverage(
                stockService.getAllStocksByMarketCap(marketCap));
    }

    public double findPriceOfSectorFund(String sector) {
        return MarketTrajectoryUtils.stockPricesAverage(stockService.getAllStocksBySector(sector));
    }

    public double findPriceOfVolatileFunds(Volatility volatility) {
        return MarketTrajectoryUtils.stockPricesAverage(
                stockService.getAllStocksByVolatility(volatility));
    }
}
