package digital.softwareshinobi.napkinexchange.market.repository;

import digital.softwareshinobi.napkinexchange.market.entity.Market;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MarketRepository extends JpaRepository<Market, Integer> {
}
