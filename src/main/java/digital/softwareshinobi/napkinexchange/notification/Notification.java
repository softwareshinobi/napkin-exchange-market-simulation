package digital.softwareshinobi.napkinexchange.notification;

import digital.softwareshinobi.napkinexchange.trader.model.Account;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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
    @Column(name = "username")
    private String username;

    @NonNull
    @Column(name = "type")
    private NotificationType type;

    @NonNull
    @Column(name = "description")
    private String description;

    public Notification(Account account, NotificationType notificationType, String string) {

        this.username = account.getUsername();

        this.type = notificationType;

        this.description = string;

    }

    public Notification(String username, NotificationType notificationType, String string) {

        this.username = username;

        this.type = notificationType;

        this.description = string;

    }
}
