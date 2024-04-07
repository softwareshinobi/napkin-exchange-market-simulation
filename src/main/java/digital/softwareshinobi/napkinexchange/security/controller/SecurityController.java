package digital.softwareshinobi.napkinexchange.security.controller;

import lombok.AllArgsConstructor;
import digital.softwareshinobi.napkinexchange.security.wrapper.StockSummaryDTO;
import digital.softwareshinobi.napkinexchange.security.enums.MarketCap;
import digital.softwareshinobi.napkinexchange.security.exception.StockNotFoundException;
import digital.softwareshinobi.napkinexchange.security.model.Security;
import digital.softwareshinobi.napkinexchange.security.wrapper.StockDTO;
import digital.softwareshinobi.napkinexchange.security.wrapper.StockPriceHistoryDTO;
import digital.softwareshinobi.napkinexchange.security.service.StockPriceHistoryService;
import digital.softwareshinobi.napkinexchange.security.service.StockService;
import java.util.ArrayList;
import java.util.Collections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

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
    public StockDTO getIndividualStockData(@PathVariable String ticker) {

        return new StockDTO(stockService.getStockByTickerSymbol(ticker));

    }

    @GetMapping(value = "/history/{ticker}")
    public List<StockPriceHistoryDTO> getIndividualStockDatahistorty(@PathVariable String ticker) {

        StockDTO dto = new StockDTO(stockService.getStockByTickerSymbol(ticker));

        List<StockPriceHistoryDTO> trimmedCandleStickList = new ArrayList();

        List<StockPriceHistoryDTO> stockPriceHistoryList = dto.getPriceHistory();

        Collections.reverse(stockPriceHistoryList); // numbers now contains [5, 4, 3, 2, 1]

        for (StockPriceHistoryDTO stockPriceHistory : stockPriceHistoryList) {

            if (trimmedCandleStickList.size() < TARGET_OUTPUT_LIST_SIZE) {

                trimmedCandleStickList.add(stockPriceHistory);

            } else {

                break;

            }

        }

        Collections.reverse(trimmedCandleStickList); // numbers now contains [5, 4, 3, 2, 1]

        return trimmedCandleStickList;

    }

    @GetMapping
    public List<StockSummaryDTO> getAllStockData() {
        return stockService.getAllStocks().stream()
                .map(StockSummaryDTO::new)
                .collect(Collectors.toList());
    }

    @GetMapping(value = "/detailed")
    public List<StockDTO> getAllDetailedStockData() {
        return stockService.getAllStocks().stream()
                .map(StockDTO::new)
                .collect(Collectors.toList());
    }

    @GetMapping(value = "/price/{ticker}")
    public double getStockPrice(@PathVariable String ticker) throws StockNotFoundException {
        return stockService.getStockPriceWithTickerSymbol(ticker);
    }

}
