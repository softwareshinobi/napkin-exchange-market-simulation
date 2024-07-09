package digital.softwareshinobi.napkinexchange.security.controller;

import digital.softwareshinobi.napkinexchange.security.exception.SecurityNotFoundException;
import digital.softwareshinobi.napkinexchange.security.model.Security;
import digital.softwareshinobi.napkinexchange.security.model.SecurityPricingHistory;
import digital.softwareshinobi.napkinexchange.security.service.SecurityHistoryService;
import digital.softwareshinobi.napkinexchange.security.service.SecurityService;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "security")
@CrossOrigin
@AllArgsConstructor
@SuppressWarnings("unused")
public class SecurityController {

    @Autowired
    private SecurityService securityService;

    @Autowired
    private SecurityHistoryService securityHistoryService;

    @GetMapping(value = "")
    public List<Security> fetchAllSecurities() {

        return this.securityService.getAllSecurities();

    }

    @GetMapping(value = "{symbol}")
    public Security fetchSecurityBySymbol(@PathVariable String symbol) {

        return this.securityService.getSecurityBySymbol(symbol);

    }

    @GetMapping(value = "/pricing/{symbol}")
    public double fetchSecurityPricingBySymbol(@PathVariable String symbol) throws SecurityNotFoundException {

        return this.securityService.getSecurityBySymbol(symbol).getPrice();

    }

    @GetMapping(value = "/history/{symbol}")
    public List<SecurityPricingHistory> fetchSecurityPricingHistoryBySymbol(@PathVariable String symbol) {

        return this.securityHistoryService.getSecurityPricingHistoryByTicker(symbol);

    }

    @GetMapping(value = "/last/{symbol}")
    public SecurityPricingHistory fetchLastSecurityPricingHistoryBySymbol(@PathVariable String symbol) {
//this is a hack

        List<SecurityPricingHistory> list = this.fetchSecurityPricingHistoryBySymbol(symbol);

        return list.get(list.size() - 1);

    }

    @GetMapping(value = "health")
    public String health() {

        return "OK";

    }

//    @GetMapping(value = "/history/{ticker}")
//    public List<StockPriceHistoryDTO> fetchSecurityPricingHistoryBySymbol(@PathVariable String ticker) {
//
//        Security security = this.stockService.getSecurityBySymbol(ticker);
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
//        return stockService.getAllSecurities().stream()
//                .map(StockSummaryDTO::new)
//                .collect(Collectors.toList());
//    }
//    @GetMapping(value = "/detailed")
//    public List<Security> getAllDetailedStockData() {
//        return stockService.getAllSecurities().stream()
//                .map(Security::new)
//                .collect(Collectors.toList());
//    }
}
