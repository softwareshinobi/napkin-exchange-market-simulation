package digital.softwareshinobi.napkinexchange.market.repository;

import digital.softwareshinobi.napkinexchange.market.model.Market;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MarketRepository extends JpaRepository<Market, Integer> {
}
