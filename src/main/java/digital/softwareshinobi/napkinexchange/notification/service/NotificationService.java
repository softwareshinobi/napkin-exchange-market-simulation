package digital.softwareshinobi.napkinexchange.notification.service;

import digital.softwareshinobi.napkinexchange.market.service.MarketActivityService;
import digital.softwareshinobi.napkinexchange.notification.model.Notification;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import digital.softwareshinobi.napkinexchange.notification.repository.NotificationRepository;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository systemNotificationRepository;

    @Autowired
    private MarketActivityService marketActivityService;

    @Autowired
    public NotificationService(NotificationRepository notificationRepository) {

        this.systemNotificationRepository = notificationRepository;

    }

    public Notification save(Notification systemNotification) {

        systemNotification.setTime(this.marketActivityService.getTime());

        return this.systemNotificationRepository.save(systemNotification);

    }

    public List<Notification> findByTrader(String trader) {

        return this.systemNotificationRepository.findByTrader(trader);

    }

    public List<Notification> findAll() {

        return this.systemNotificationRepository.findAll();

    }

}
