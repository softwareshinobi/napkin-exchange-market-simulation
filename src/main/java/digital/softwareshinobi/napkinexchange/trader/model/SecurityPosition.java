package digital.softwareshinobi.napkinexchange.trader.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import digital.softwareshinobi.napkinexchange.trader.utility.CalculateCostBasisAndProfits;
import jakarta.persistence.*;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/*
    Entity for handling the stocks that a user owns
    Includes the ticker symbol and amount owned for each stock type, as well as
    a call back to the account username for sorting each inventory
 */
@Entity
@Table(name = "stock_owned")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SecurityPosition implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonIgnore
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "account_id", nullable = false)
    @JsonBackReference
    private Trader trader;

    @Column(name = "symbol")
    private String symbol;

    @Column(name = "units")
    private Integer units;

    @Column(name = "cost_basis")
    private Double costBasis;
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

        this.setCostBasis(CalculateCostBasisAndProfits.newCostBasis(
                this.units,
                numberUnits,
                this.costBasis,
                currentSecurityPrice));

        this.setUnits(this.units + numberUnits);
        
    //    this.setValue(this.costBasis * this.amountOwned);

    }

}
