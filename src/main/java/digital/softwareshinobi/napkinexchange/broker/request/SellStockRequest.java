package digital.softwareshinobi.napkinexchange.broker.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SellStockRequest {

    private String username;

    private String security;

    private Integer units;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append("username=").append(username);
        sb.append(", security=").append(security);
        sb.append(", units=").append(units);
        sb.append('}');
        return sb.toString();
    }

}
