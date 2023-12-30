package digital.softwareshinobi.napkinexchange.account.repository;

import digital.softwareshinobi.napkinexchange.account.model.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, String> {
}
