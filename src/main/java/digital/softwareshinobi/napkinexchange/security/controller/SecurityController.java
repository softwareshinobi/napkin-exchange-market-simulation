package digital.softwareshinobi.napkinexchange.security.controller;

//this.priceHistory = stock.getPriceHistory().stream()
//.map(StockPriceHistoryDTO::new)
//.collect(Collectors.toList());
import digital.softwareshinobi.napkinexchange.security.exception.StockNotFoundException;
import digital.softwareshinobi.napkinexchange.security.model.Security;
import digital.softwareshinobi.napkinexchange.security.service.StockPriceHistoryService;
import digital.softwareshinobi.napkinexchange.security.service.StockService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "candlestick")
@CrossOrigin
@AllArgsConstructor
@SuppressWarnings("unused")
public class SecurityController {

    @Autowired
    private StockService stockService;

    @Autowired
    private StockPriceHistoryService stockPriceHistoryService;

    private static final int TARGET_OUTPUT_LIST_SIZE = 100;

    @GetMapping(value = "/{ticker}")
    public Security getIndividualStockData(@PathVariable String ticker) {

        return this.stockService.getStockByTickerSymbol(ticker);

    }

    @GetMapping(value = "/price/{ticker}")
    public double getStockPrice(@PathVariable String ticker) throws StockNotFoundException {

        return this.stockService.getStockPriceWithTickerSymbol(ticker);

    }

//    @GetMapping(value = "/history/{ticker}")
//    public List<StockPriceHistoryDTO> getIndividualStockDatahistorty(@PathVariable String ticker) {
//
//        Security security = this.stockService.getStockByTickerSymbol(ticker);
//
//        List<StockPriceHistoryDTO> trimmedCandleStickList = new ArrayList();
//
//     //   List<StockPriceHistoryDTO> stockPriceHistoryList = security.getPriceHistory();
//
////        Collections.reverse(stockPriceHistoryList); // numbers now contains [5, 4, 3, 2, 1]
//
//        for (StockPriceHistoryDTO stockPriceHistory : stockPriceHistoryList) {
//
//            if (trimmedCandleStickList.size() < TARGET_OUTPUT_LIST_SIZE) {
//
//                trimmedCandleStickList.add(stockPriceHistory);
//
//            } else {
//
//                break;
//
//            }
//
//        }
//
//        Collections.reverse(trimmedCandleStickList); // numbers now contains [5, 4, 3, 2, 1]
//
//        return trimmedCandleStickList;
//
//    }
//    @GetMapping
//    public List<StockSummaryDTO> getAllStockData() {
//        return stockService.getAllStocks().stream()
//                .map(StockSummaryDTO::new)
//                .collect(Collectors.toList());
//    }
//    @GetMapping(value = "/detailed")
//    public List<Security> getAllDetailedStockData() {
//        return stockService.getAllStocks().stream()
//                .map(Security::new)
//                .collect(Collectors.toList());
//    }
}
