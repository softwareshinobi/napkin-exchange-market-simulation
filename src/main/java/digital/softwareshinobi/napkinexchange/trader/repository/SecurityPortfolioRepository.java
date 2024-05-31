package digital.softwareshinobi.napkinexchange.trader.repository;

import digital.softwareshinobi.napkinexchange.trader.portfolio.SecurityPosition;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SecurityPortfolioRepository extends JpaRepository<SecurityPosition, String> {
}
