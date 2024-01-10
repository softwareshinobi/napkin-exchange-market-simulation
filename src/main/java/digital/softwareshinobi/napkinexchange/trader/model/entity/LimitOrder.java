package digital.softwareshinobi.napkinexchange.trader.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import digital.softwareshinobi.napkinexchange.ticker.entity.Stock;
import digital.softwareshinobi.napkinexchange.trader.exception.AccountBalanceException;
import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table
@Getter
@Setter
@NoArgsConstructor
public class LimitOrder implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonIgnore
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    private Account account;

    @ManyToOne(cascade = CascadeType.ALL)
    private Stock stock;

    @Column
    private Integer sharesToBuy;

    @Column
    private Double limitPrice;

    @Column
    private String limitOrderType;

    public LimitOrder(String limitOrderType, Account account, Stock stock, int sharesToBuy, double limitPrice) {

        this.limitOrderType = limitOrderType;

        this.account = account;

        this.sharesToBuy = sharesToBuy;

        this.stock = stock;

        this.limitPrice = limitPrice;

        if (!validOrderRequest()) {

            throw new AccountBalanceException("Cannot Process Order");

        }

    }

    public boolean validOrderRequest() {

        return !(sharesToBuy * stock.getPrice() > account.getAccountBalance());

    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("LimitOrder{");
        sb.append("id=").append(id);
        //sb.append(", account=").append(account);
        sb.append(", stock=").append(stock);
        sb.append(", sharesToBuy=").append(sharesToBuy);
        sb.append(", limitPrice=").append(limitPrice);
        sb.append(", limitOrderType=").append(limitOrderType);
        sb.append('}');
        return sb.toString();
    }

}
