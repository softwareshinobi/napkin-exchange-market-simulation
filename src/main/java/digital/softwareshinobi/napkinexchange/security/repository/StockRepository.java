package digital.softwareshinobi.napkinexchange.security.repository;

import digital.softwareshinobi.napkinexchange.security.model.Stock;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockRepository extends JpaRepository<Stock, String> {
}
