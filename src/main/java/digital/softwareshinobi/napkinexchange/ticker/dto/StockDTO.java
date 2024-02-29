package digital.softwareshinobi.napkinexchange.ticker.dto;

import java.util.List;
import java.util.stream.Collectors;

import digital.softwareshinobi.napkinexchange.ticker.entity.Stock;
import digital.softwareshinobi.napkinexchange.trader.utility.CalculateCostBasisAndProfits;
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
  this.priceHistory = stock.getPriceHistory().stream()
                .map(StockPriceHistoryDTO::new)
                .collect(Collectors.toList());

        this.percentChange = getPercentChange(this.getPrice(), this.getLastDayPrice());

    }

    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getSector() {
        return sector;
    }

    public void setSector(String sector) {
        this.sector = sector;
    }

    public String getMarketCap() {
        return marketCap;
    }

    public void setMarketCap(String marketCap) {
        this.marketCap = marketCap;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getLastDayPrice() {
        return lastDayPrice;
    }

    public void setLastDayPrice(double lastDayPrice) {
        this.lastDayPrice = lastDayPrice;
    }

    public double getPercentChange() {
        return percentChange;
    }

    public void setPercentChange(double percentChange) {
        this.percentChange = percentChange;
    }

    public int getMomentum() {
        return momentum;
    }

    public void setMomentum(int momentum) {
        this.momentum = momentum;
    }

    public int getMomentumStreakInDays() {
        return momentumStreakInDays;
    }

    public void setMomentumStreakInDays(int momentumStreakInDays) {
        this.momentumStreakInDays = momentumStreakInDays;
    }

    public String getVolatileStock() {
        return volatileStock;
    }

    public void setVolatileStock(String volatileStock) {
        this.volatileStock = volatileStock;
    }

    public String getInvestorRating() {
        return investorRating;
    }

    public void setInvestorRating(String investorRating) {
        this.investorRating = investorRating;
    }

    public List<StockPriceHistoryDTO> getPriceHistory() {
        return priceHistory;
    }

    public void setPriceHistory(List<StockPriceHistoryDTO> priceHistory) {
        this.priceHistory = priceHistory;
    }

    public double getPercentChange(double currentPrice, double lastDayPrice) {

        return CalculateCostBasisAndProfits.roundToTwoDecimalPlaces(
                (currentPrice - lastDayPrice) / lastDayPrice * 100);

    }
}
