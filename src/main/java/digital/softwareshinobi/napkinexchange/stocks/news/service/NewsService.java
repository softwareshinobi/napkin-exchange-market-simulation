package digital.softwareshinobi.napkinexchange.stocks.news.service;

import lombok.AllArgsConstructor;
import digital.softwareshinobi.napkinexchange.stocks.news.entity.News;
import digital.softwareshinobi.napkinexchange.stocks.news.repository.NewsRepository;
import digital.softwareshinobi.napkinexchange.stocks.stock.entity.Stock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class NewsService {

    @Autowired
    private NewsRepository newsRepository;

    public List<News> findAllNews() {
        return newsRepository.findAll();
    }

    public void saveNewsForStock(Stock stock, String newsEvent, ZonedDateTime date) {
        newsRepository.save(new News(stock, newsEvent, date));
    }
}
