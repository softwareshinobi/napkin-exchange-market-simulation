package digital.softwareshinobi.napkinexchange.security.repository;

import digital.softwareshinobi.napkinexchange.security.model.SecurityPricingHistory;
import digital.softwareshinobi.napkinexchange.security.model.SecurityPricingHistoryId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface SecurityPricingHistoryRepository extends JpaRepository<SecurityPricingHistory, SecurityPricingHistoryId> {

    @Modifying
    @Query(value = "truncate table stock_history", nativeQuery = true)
    void truncateTable();
}
