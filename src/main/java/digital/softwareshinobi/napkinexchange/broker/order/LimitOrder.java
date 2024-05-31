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
    
    @Column
    public Integer partnerID;
    
    @ManyToOne(cascade = CascadeType.ALL)
    private Trader trader;

    @Column
    private String type;

    @ManyToOne(cascade = CascadeType.ALL)
    private Security security;

    @Column
    private Integer units;

    @Column
    private Double strike;



    public LimitOrder(String type, Trader trader, Security security, int units, double strike) {

        this.type = type;

        this.trader = trader;

        this.units = units;

        this.security = security;

        this.strike = strike;

    }
//
//    @Override
//    public String toS66tring() {
//
//        StringBuilder stringBuilder = new StringBuilder();
//
//        stringBuilder.append("{");
//        stringBuilder.append("id=").append(id);
//        stringBuilder.append(", account=").append(trader);
//        stringBuilder.append(", type=").append(type);
//        stringBuilder.append(", stock=").append(security);
//        stringBuilder.append(", sharesToBuy=").append(units);
//        stringBuilder.append(", strikePrice=").append(strike);
//        stringBuilder.append(", relatedOrderId=").append(partnerID);
//        stringBuilder.append('}');
//
//        String returnString = stringBuilder.toString();
//
//        stringBuilder = null;
//
//        return returnString;
//
//    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("LimitOrder{");
        sb.append("id=").append(id);
        sb.append(", partnerID=").append(partnerID);
        sb.append(", trader=").append(trader);
        sb.append(", type=").append(type);
        sb.append(", security=").append(security);
        sb.append(", units=").append(units);
        sb.append(", strike=").append(strike);
        sb.append('}');
        return sb.toString();
    }

}
