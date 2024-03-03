package digital.softwareshinobi.napkinexchange.trader.model;

import digital.softwareshinobi.napkinexchange.security.entity.Stock;
import digital.softwareshinobi.napkinexchange.trader.exception.AccountBalanceException;
import jakarta.persistence.*;
import java.io.Serializable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table
@Getter
@Setter
@NoArgsConstructor
public class LimitOrder implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    //@JsonIgnore
    private Integer id;

    @ManyToOne(cascade = CascadeType.ALL)
    private Account account;

    @Column
    private String type;

    @ManyToOne(cascade = CascadeType.ALL)
    private Stock stock;

    @Column
    private Integer sharesToBuy;

    @Column
    private Double strikePrice;

    @Column
    public Integer relatedOrderId;

    public LimitOrder(String limitOrderType, Account account, Stock stock, int sharesToBuy, double limitPrice) {

        this.type = limitOrderType;

        this.account = account;

        this.sharesToBuy = sharesToBuy;

        this.stock = stock;

        this.strikePrice = limitPrice;

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
        sb.append(", account=").append(account);
        sb.append(", type=").append(type);
        sb.append(", stock=").append(stock);
        sb.append(", sharesToBuy=").append(sharesToBuy);
        sb.append(", strikePrice=").append(strikePrice);
        sb.append(", relatedOrderId=").append(relatedOrderId);
        sb.append('}');
        return sb.toString();
    }

}
