package digital.softwareshinobi.napkinexchange.security.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.io.Serializable;
import java.time.ZonedDateTime;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
@Embeddable
@AllArgsConstructor
@NoArgsConstructor
public class SecurityPricingHistoryId implements Serializable {

    @Column(name = "date_time")
    @Temporal(TemporalType.TIMESTAMP)
    private ZonedDateTime dateTime;

    @Column(name = "symbol")
    private String symbol;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("SecurityPricingHistoryId{");
        sb.append("marketDate=").append(dateTime);
        sb.append(", ticker=").append(symbol);
        sb.append('}');
        return sb.toString();
    }

}
