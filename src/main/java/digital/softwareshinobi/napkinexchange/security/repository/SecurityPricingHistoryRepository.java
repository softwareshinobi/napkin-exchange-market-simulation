package digital.softwareshinobi.napkinexchange.security.repository;

import digital.softwareshinobi.napkinexchange.security.model.SecurityPricingHistory;
import digital.softwareshinobi.napkinexchange.security.model.SecurityPricingHistoryId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SecurityPricingHistoryRepository extends JpaRepository<SecurityPricingHistory, SecurityPricingHistoryId> {

}
