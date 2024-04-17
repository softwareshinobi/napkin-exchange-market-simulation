package digital.softwareshinobi.napkinexchange.security.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.*;
import java.io.Serializable;

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
}
