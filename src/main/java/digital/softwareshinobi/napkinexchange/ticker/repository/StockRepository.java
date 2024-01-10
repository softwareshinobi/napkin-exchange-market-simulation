package digital.softwareshinobi.napkinexchange.ticker.repository;

import digital.softwareshinobi.napkinexchange.ticker.entity.Stock;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockRepository extends JpaRepository<Stock, String> {
}
