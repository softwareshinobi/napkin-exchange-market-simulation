package digital.softwareshinobi.napkinexchange.notification.service;

import digital.softwareshinobi.napkinexchange.notification.model.Notification;
import digital.softwareshinobi.napkinexchange.notification.repository.NotificationRepository;
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

    public List<Notification> findByUsername(String username) {

        return this.notificationRepository.findByUsername(username);

    }

    public List<Notification> findAll() {

        return this.notificationRepository.findAll();

    }

}
