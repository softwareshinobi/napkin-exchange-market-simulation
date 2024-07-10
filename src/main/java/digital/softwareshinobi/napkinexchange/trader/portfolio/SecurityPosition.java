package digital.softwareshinobi.napkinexchange.trader.portfolio;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import digital.softwareshinobi.napkinexchange.trader.model.Trader;
import digital.softwareshinobi.napkinexchange.trader.utility.CalculateCostBasisAndProfits;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "security_position")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SecurityPosition implements Serializable {

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
    @Column(name = "value")
    public Double value;

    public SecurityPosition(Trader trader, String symbol, Integer units, Double costBasis) {

        this.trader = trader;

        this.symbol = symbol;

        this.units = units;

        this.costBasis = costBasis;

        this.value = units * costBasis;
    }

    public void updateCostBasisAndAmountOwned(int numberUnits, double currentSecurityPrice) {

        this.costBasis = CalculateCostBasisAndProfits.newCostBasis(
                this.units,
                numberUnits,
                this.costBasis,
                currentSecurityPrice);

        this.units = this.units + numberUnits;

    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 31 * hash + Objects.hashCode(this.id);
        hash = 31 * hash + Objects.hashCode(this.trader);
        hash = 31 * hash + Objects.hashCode(this.symbol);
        hash = 31 * hash + Objects.hashCode(this.units);
        hash = 31 * hash + Objects.hashCode(this.costBasis);
        hash = 31 * hash + Objects.hashCode(this.value);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final SecurityPosition other = (SecurityPosition) obj;
        if (!Objects.equals(this.symbol, other.symbol)) {
            return false;
        }
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.trader, other.trader)) {
            return false;
        }
        if (!Objects.equals(this.units, other.units)) {
            return false;
        }
        if (!Objects.equals(this.costBasis, other.costBasis)) {
            return false;
        }
        return Objects.equals(this.value, other.value);
    }

    @Override
    public String toString() {
      
        StringBuilder stringBuilder = new StringBuilder();
        
        stringBuilder.append("SecurityPosition{");
        stringBuilder.append("id=").append(id);
        stringBuilder.append(", symbol=").append(symbol);
        stringBuilder.append(", units=").append(units);
        stringBuilder.append(", costBasis=").append(costBasis);
         stringBuilder.append(", value=").append(value);
        stringBuilder.append('}');
        
        return stringBuilder.toString();
    
    }

}
