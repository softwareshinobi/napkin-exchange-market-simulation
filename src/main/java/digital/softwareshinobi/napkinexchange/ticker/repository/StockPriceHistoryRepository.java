package digital.softwareshinobi.napkinexchange.ticker.repository;

import digital.softwareshinobi.napkinexchange.ticker.entity.StockPriceHistory;
import digital.softwareshinobi.napkinexchange.ticker.entity.idclass.StockPriceHistoryId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface StockPriceHistoryRepository extends JpaRepository<StockPriceHistory, StockPriceHistoryId> {

    @Modifying
    @Query(value = "truncate table stock_history", nativeQuery = true)
    void truncateTable();
}
