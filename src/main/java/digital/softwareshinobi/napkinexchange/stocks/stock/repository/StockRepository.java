package digital.softwareshinobi.napkinexchange.stocks.stock.repository;

import digital.softwareshinobi.napkinexchange.stocks.stock.entity.Stock;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockRepository extends JpaRepository<Stock, String> {
}
