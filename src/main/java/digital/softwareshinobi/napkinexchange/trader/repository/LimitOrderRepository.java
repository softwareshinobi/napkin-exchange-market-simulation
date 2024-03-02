package digital.softwareshinobi.napkinexchange.trader.repository;

import digital.softwareshinobi.napkinexchange.trader.model.LimitOrder;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface LimitOrderRepository extends JpaRepository<LimitOrder, Integer> {

    @Modifying
    @Query(value = "truncate table limit_order", nativeQuery = true)
    public void truncateTable();

    public Optional<LimitOrder> findById(Integer id);
}
