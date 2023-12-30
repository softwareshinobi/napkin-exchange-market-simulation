package digital.softwareshinobi.napkinexchange.stocks.earnings.repository;

import digital.softwareshinobi.napkinexchange.stocks.earnings.entity.EarningsReport;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EarningsRepository extends JpaRepository<EarningsReport, Long> {
}
