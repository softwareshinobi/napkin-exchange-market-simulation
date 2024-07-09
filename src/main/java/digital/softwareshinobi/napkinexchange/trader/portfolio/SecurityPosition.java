package digital.softwareshinobi.napkinexchange.trader.portfolio;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import digital.softwareshinobi.napkinexchange.trader.model.Trader;
import digital.softwareshinobi.napkinexchange.trader.utility.CalculateCostBasisAndProfits;
import jakarta.persistence.*;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.slf4j.LoggerFactory;

@Entity
@Table(name = "security_positions")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SecurityPosition implements Serializable {

    private final org.slf4j.Logger logger = LoggerFactory.getLogger(SecurityPosition.class);

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonIgnore
    public Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "account_id", nullable = false)
    @JsonBackReference
    private Trader trader;

    @Column(name = "symbol")
    public String symbol;

    @Column(name = "units")
    public Integer units;

    @Column(name = "cost_basis")
    public Double costBasis;
//
//    @Column(name = "value")
//    public Double value;
//
//    @Column(name = "price")
//    public Double price;
//
//     @JsonIgnore
//     @Column(name = "value")
//    public Double value;

    public SecurityPosition(Trader trader, String symbol, Integer units, Double costBasis) {

        this.trader = trader;

        this.symbol = symbol;

        this.units = units;

        this.costBasis = costBasis;

        //  this.value = units * costBasis;
    }

    public void updateCostBasisAndAmountOwned(int numberUnits, double currentSecurityPrice) {

        // logger.debug("cost / before / "+ this.costBasis);
        //  logger.debug("units / before / "+ this.units);
        //  logger.debug("current price / "+ currentSecurityPrice);
        this.costBasis = CalculateCostBasisAndProfits.newCostBasis(
                this.units,
                numberUnits,
                this.costBasis,
                currentSecurityPrice);

        this.units = this.units + numberUnits;

        //   logger.debug("cost / after / "+ this.costBasis);
        //   logger.debug("units / after / "+ this.units);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("SecurityPosition{");
        sb.append("id=").append(id);
        sb.append(", symbol=").append(symbol);
        sb.append(", units=").append(units);
        sb.append(", costBasis=").append(costBasis);
        //    sb.append(", value=").append(value);
        //   sb.append(", price=").append(price);
        sb.append('}');
        return sb.toString();
    }

}
