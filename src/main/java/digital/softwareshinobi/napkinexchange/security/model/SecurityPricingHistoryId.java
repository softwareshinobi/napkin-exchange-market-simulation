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

    @Column(name = "market_date")
    @Temporal(TemporalType.TIMESTAMP)
    private ZonedDateTime marketDate;

    @Column(name = "ticker")
    private String ticker;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("SecurityPricingHistoryId{");
        sb.append("marketDate=").append(marketDate);
        sb.append(", ticker=").append(ticker);
        sb.append('}');
        return sb.toString();
    }

}
