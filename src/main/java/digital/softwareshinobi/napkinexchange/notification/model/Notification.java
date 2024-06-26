package digital.softwareshinobi.napkinexchange.notification.model;

import digital.softwareshinobi.napkinexchange.notification.type.NotificationType;
import digital.softwareshinobi.napkinexchange.trader.model.Trader;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.ZonedDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "notification")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NonNull
    @Column(name = "time")
    private ZonedDateTime time;

    @NonNull
    @Column(name = "trader")
    private String trader;

    @NonNull
    @Column(name = "type")
    private NotificationType type;

    @NonNull
    @Column(name = "message", columnDefinition = "VARCHAR(1000)")
    private String message;

    public Notification(Trader trader, NotificationType notificationType, String message) {

        this.trader = trader.getUsername();

        this.type = notificationType;

        this.message = message;

    }

    public Notification(String trader, NotificationType notificationType, String message) {

        this.trader = trader;

        this.type = notificationType;

        this.message = message;

    }

    public Notification(String trader, NotificationType notificationType, Object payload) {

        this.trader = trader;

        this.type = notificationType;

        this.message = payload.toString();

    }

}
