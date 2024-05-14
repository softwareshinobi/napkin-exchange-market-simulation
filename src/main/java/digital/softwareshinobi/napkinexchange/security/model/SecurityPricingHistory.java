package digital.softwareshinobi.napkinexchange.security.model;

import jakarta.persistence.*;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "security_pricing_history")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SecurityPricingHistory implements Serializable {

    @EmbeddedId
    private SecurityPricingHistoryId id;

    @MapsId(value = "symbol")
    @ManyToOne(fetch = FetchType.EAGER)
    private Security security;

    @Column(name = "price")
    private Double price;

    @Column(name = "previousprice")
    private Double previousprice;

    @Column(name = "gainPercent")
    private Double gainPercent;

    @Column(name = "gainValue")
    private Double gainValue;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("SecurityPricingHistory{");
        sb.append("id=").append(id);
        sb.append(", security=").append(security);
        sb.append(", price=").append(price);
        sb.append('}');
        return sb.toString();
    }
}
