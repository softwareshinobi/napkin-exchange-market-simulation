package digital.softwareshinobi.napkinexchange.trader.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.io.Serializable;
import java.time.ZonedDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@IdClass(AccountHistoryId.class)
@AllArgsConstructor
@Table(name = "account_history")
public class AccountHistory implements Serializable {

    @Id
    @Temporal(TemporalType.TIMESTAMP)
    private ZonedDateTime date;

    @Id
    @JsonBackReference
    private Trader account;

    @Column(name = "account_balance")
    private Double balance;

    @Column(name = "portfolioValue")
    private Double portfolioValue;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("AccountHistory{");
        sb.append("date=").append(date);
        sb.append(", account=").append(account);
        sb.append(", balance=").append(balance);
        sb.append(", portfolioValue=").append(portfolioValue);
        sb.append('}');
        return sb.toString();
    }

}
