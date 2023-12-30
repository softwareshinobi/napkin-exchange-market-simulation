package digital.softwareshinobi.napkinexchange.stocks.news.repository;

import digital.softwareshinobi.napkinexchange.stocks.news.entity.News;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NewsRepository extends JpaRepository<News, Long> {
}
