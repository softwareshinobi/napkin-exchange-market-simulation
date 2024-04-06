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
public class StockOwned implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonIgnore
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "account_id", nullable = false)
    @JsonBackReference
    private Account account;

    @Column(name = "ticker")
    private String ticker;

    @Column(name = "owned")
    private Integer amountOwned;

    @Column(name = "cost_basis")
    private Double costBasis;

    public StockOwned(Account account, String ticker, int amountOwned, double costBasis) {

        this.account = account;

        this.ticker = ticker;

        this.amountOwned = amountOwned;

        this.costBasis = costBasis;

    }

    public void updateCostBasisAndAmountOwned(int numberUnits, double currentSecurityPrice) {

        this.setCostBasis(CalculateCostBasisAndProfits.newCostBasis(
                this.amountOwned,
                numberUnits,
                this.costBasis,
                currentSecurityPrice));

        this.setAmountOwned(this.amountOwned + numberUnits);

    }

//    @Override
//    public String toString() {
//        StringBuilder sb = new StringBuilder();
//        sb.append("{");
//        sb.append("id=").append(id);
//        sb.append(", account=").append(account);
//        sb.append(", ticker=").append(ticker);
//        sb.append(", amountOwned=").append(amountOwned);
//        sb.append(", costBasis=").append(costBasis);
//        sb.append('}');
//        return sb.toString();
//    }
}
