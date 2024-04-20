package digital.softwareshinobi.napkinexchange.security.model;

import jakarta.persistence.*;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/*
Saves daily stock history for one year.
 */
@Entity
@Table(name = "stock_history")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SecurityPricingHistory implements Serializable {

    @EmbeddedId
    private SecurityPricingHistoryId id;

    @MapsId(value = "ticker")
    @ManyToOne(fetch = FetchType.LAZY)
    private Security stock;

    @Column(name = "price")
    private Double stockPrice;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("SecurityPricingHistory{");
        sb.append("id=").append(id);
        sb.append(", stock=").append(stock);
        sb.append(", stockPrice=").append(stockPrice);
        sb.append('}');
        return sb.toString();
    }
}
