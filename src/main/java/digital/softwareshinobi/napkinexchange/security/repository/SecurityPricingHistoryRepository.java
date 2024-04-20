package digital.softwareshinobi.napkinexchange.security.repository;

import digital.softwareshinobi.napkinexchange.security.model.Security;
import digital.softwareshinobi.napkinexchange.security.model.SecurityPricingHistory;
import digital.softwareshinobi.napkinexchange.security.model.SecurityPricingHistoryId;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SecurityPricingHistoryRepository extends JpaRepository<SecurityPricingHistory, SecurityPricingHistoryId> {

    Optional<SecurityPricingHistory> findTopBySecurityOrderByIdDesc(Security security);

}
