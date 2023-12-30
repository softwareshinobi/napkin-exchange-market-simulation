package digital.softwareshinobi.napkinexchange.stocks.news.controller;

import lombok.AllArgsConstructor;
import digital.softwareshinobi.napkinexchange.stocks.news.entity.News;
import digital.softwareshinobi.napkinexchange.stocks.news.service.NewsService;
import digital.softwareshinobi.napkinexchange.stocks.stock.exception.StockNotFoundException;
import digital.softwareshinobi.napkinexchange.stocks.stock.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/news")
@AllArgsConstructor
public class NewsController {

    @Autowired
    private final StockService stockService;
    @Autowired
    private final NewsService newsService;

    @RequestMapping(value = "/{ticker}")
    public List<News> findStockNewsWithTicker(@PathVariable String ticker) throws StockNotFoundException {
        return stockService.getStockByTickerSymbol(ticker).getNewsHistory();
    }

    @GetMapping
    public List<News> findAllMarketNews() {
        return newsService.findAllNews();
    }
}
