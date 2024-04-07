package digital.softwareshinobi.napkinexchange.trader.repository;

import digital.softwareshinobi.napkinexchange.broker.order.LimitOrder;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LimitOrderRepository extends JpaRepository<LimitOrder, Integer> {

    public Optional<LimitOrder> findById(Integer id);

}
