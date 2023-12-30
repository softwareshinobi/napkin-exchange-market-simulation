package digital.softwareshinobi.napkinexchange.account.repository;

import digital.softwareshinobi.napkinexchange.account.model.entity.LimitOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface LimitOrderRepository extends JpaRepository<LimitOrder, Long> {

    @Modifying
    @Query(value = "truncate table limit_order", nativeQuery = true)
    void truncateTable();
}
