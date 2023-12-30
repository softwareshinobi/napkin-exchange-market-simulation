package digital.softwareshinobi.napkinexchange.account.repository;

import digital.softwareshinobi.napkinexchange.account.model.entity.StockOwned;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockOwnedRepository extends JpaRepository<StockOwned, String> {
}
