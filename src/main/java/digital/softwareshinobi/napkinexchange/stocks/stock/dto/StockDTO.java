package digital.softwareshinobi.napkinexchange.stocks.stock.dto;

import java.util.List;
import java.util.stream.Collectors;

import digital.softwareshinobi.napkinexchange.stocks.earnings.dto.EarningsDTO;
import digital.softwareshinobi.napkinexchange.stocks.news.dto.NewsDTO;
import digital.softwareshinobi.napkinexchange.stocks.stock.entity.Stock;
import digital.softwareshinobi.napkinexchange.account.utils.CalculateCostBasisAndProfits;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class StockDTO {

    private String ticker;
    private String companyName;
    private String sector;
    private String marketCap;
    private double price;
    private double lastDayPrice;
    private double percentChange;
    private int momentum;
    private int momentumStreakInDays;
    private String volatileStock;
    private String investorRating;
    private List<NewsDTO> newsHistory;
    private List<EarningsDTO> earningsHistory;
    private List<StockPriceHistoryDTO> priceHistory;

    public StockDTO(Stock stock) {
        this.ticker = stock.getTicker();
        this.companyName = stock.getCompanyName();
        this.sector = stock.getSector();
        this.marketCap = String.valueOf(stock.getMarketCap());
        this.price = stock.getPrice();
        this.lastDayPrice = stock.getLastDayPrice();
        this.momentum = stock.getMomentum();
        this.momentumStreakInDays = stock.getMomentumStreakInDays();
        this.volatileStock = String.valueOf(stock.getVolatileStock());
        this.investorRating = String.valueOf(stock.getInvestorRating());
        this.newsHistory = stock.getNewsHistory().stream()
                .map(NewsDTO::new)
                .collect(Collectors.toList());
        this.earningsHistory = stock.getEarningsHistory().stream()
                .map(EarningsDTO::new)
                .collect(Collectors.toList());
        this.priceHistory = stock.getPriceHistory().stream()
                .map(StockPriceHistoryDTO::new)
                .collect(Collectors.toList());
        this.percentChange = getPercentChange(this.getPrice(), this.getLastDayPrice());
    }

    public double getPercentChange(double currentPrice, double lastDayPrice) {
        if (lastDayPrice == 0) {
            return 0.0;
        }
        return CalculateCostBasisAndProfits.roundToTwoDecimalPlaces(
                (currentPrice - lastDayPrice) / lastDayPrice * 100);
    }
}
