package digital.softwareshinobi.napkinexchange.account.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;

@Getter
@Setter
@EqualsAndHashCode
public class AccountHistoryId implements Serializable {

    @Column(name = "market_date")
    private ZonedDateTime date;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "account_id", nullable = false)
    @JsonBackReference
    private Account account;

}
