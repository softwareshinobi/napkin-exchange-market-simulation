package digital.softwareshinobi.napkinexchange.notification;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    NotificationRepository notificationRepository;

    @Autowired
    public NotificationService(NotificationRepository notificationRepository) {

        this.notificationRepository = notificationRepository;

    }

    public Notification save(Notification notification) {

        return this.notificationRepository.save(notification);

    }

    List<Notification> findByUsername(String username) {

        return this.notificationRepository.findByUsername(username);

    }

    List<Notification> findAll() {

        return this.notificationRepository.findAll();

    }

}
