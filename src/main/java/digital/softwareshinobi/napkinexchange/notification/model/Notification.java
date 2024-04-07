package digital.softwareshinobi.napkinexchange.notification.model;

import digital.softwareshinobi.napkinexchange.trader.model.Trader;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.Date;
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

    //@NonNull
    @Column(name = "time")
    private String time = new Date().toGMTString();

    @NonNull
    @Column(name = "username")
    private String username;

    @NonNull
    @Column(name = "type")
    private NotificationType type;

    @NonNull
    @Column(name = "description", columnDefinition = "VARCHAR(1000)")
    private String description;

    public Notification(Trader account, NotificationType notificationType, String string) {

        this.username = account.getUsername();

        this.type = notificationType;

        this.description = string;

    }

    public Notification(String username, NotificationType notificationType, String string) {

        this.username = username;

        this.type = notificationType;

        this.description = string;

    }

    public Notification(String username, NotificationType notificationType, Object object) {

        this.username = username;

        this.type = notificationType;

        this.description = object.toString();

    }

}
