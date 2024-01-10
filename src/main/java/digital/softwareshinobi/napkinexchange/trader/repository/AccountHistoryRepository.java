package digital.softwareshinobi.napkinexchange.trader.repository;

import digital.softwareshinobi.napkinexchange.trader.model.entity.AccountHistory;
import digital.softwareshinobi.napkinexchange.trader.model.entity.AccountHistoryId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface AccountHistoryRepository extends JpaRepository<AccountHistory, AccountHistoryId> {

    @Modifying
    @Query(value = "truncate table account_history", nativeQuery = true)
    void truncateTable();
}
