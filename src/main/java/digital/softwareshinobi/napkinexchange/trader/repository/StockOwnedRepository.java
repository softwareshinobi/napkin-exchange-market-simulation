package digital.softwareshinobi.napkinexchange.trader.repository;

import digital.softwareshinobi.napkinexchange.trader.model.entity.StockOwned;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockOwnedRepository extends JpaRepository<StockOwned, String> {
}
