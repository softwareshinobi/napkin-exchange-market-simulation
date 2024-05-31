package digital.softwareshinobi.napkinexchange.broker.order;

import digital.softwareshinobi.napkinexchange.security.model.Security;
import digital.softwareshinobi.napkinexchange.trader.model.Trader;
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
    private Trader account;

    @Column
    private String type;

    @ManyToOne(cascade = CascadeType.ALL)
    private Security stock;

    @Column
    private Integer sharesToBuy;

    @Column
    private Double strikePrice;

    @Column
    public Integer relatedOrderId;

    public LimitOrder(String limitOrderType, Trader account, Security stock, int sharesToBuy, double limitPrice) {

        System.out.println("here");

        this.type = limitOrderType;

        this.account = account;

        this.sharesToBuy = sharesToBuy;

        this.stock = stock;

        this.strikePrice = limitPrice;

    }

    @Override
    public String toString() {

        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("{");
        stringBuilder.append("id=").append(id);
        stringBuilder.append(", account=").append(account);
        stringBuilder.append(", type=").append(type);
        stringBuilder.append(", stock=").append(stock);
        stringBuilder.append(", sharesToBuy=").append(sharesToBuy);
        stringBuilder.append(", strikePrice=").append(strikePrice);
        stringBuilder.append(", relatedOrderId=").append(relatedOrderId);
        stringBuilder.append('}');

        String returnString = stringBuilder.toString();

        stringBuilder = null;

        return returnString;

    }

}
