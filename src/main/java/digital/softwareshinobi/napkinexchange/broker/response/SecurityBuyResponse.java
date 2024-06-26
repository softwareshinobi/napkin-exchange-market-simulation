package digital.softwareshinobi.napkinexchange.broker.response;

import digital.softwareshinobi.napkinexchange.broker.request.SecurityBuyRequest;
import digital.softwareshinobi.napkinexchange.security.model.Security;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SecurityBuyResponse {

    private String trader;

    private String ticker;

    private Integer units;

    private Double price;

    public SecurityBuyResponse(SecurityBuyRequest a, Security security) {

        this.trader = a.getUsername();

        this.ticker = a.getTicker();

        this.units = a.getUnits();

        this.price = security.getPrice();

    }

    @Override
    public String toString() {

        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("SecurityBuyResponse{");
        stringBuilder.append("trader=").append(trader);
        stringBuilder.append(", ticker=").append(ticker);
        stringBuilder.append(", units=").append(units);
        stringBuilder.append(", price=").append(price);
        stringBuilder.append('}');

        return stringBuilder.toString();
    }

}
