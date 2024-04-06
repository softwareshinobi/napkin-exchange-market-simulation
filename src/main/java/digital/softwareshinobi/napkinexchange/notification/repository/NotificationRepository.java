package digital.softwareshinobi.napkinexchange.notification.repository;

import digital.softwareshinobi.napkinexchange.notification.model.Notification;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    public List<Notification> findByUsername(String username);

}
