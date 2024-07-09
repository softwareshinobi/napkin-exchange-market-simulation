package digital.softwareshinobi.napkinexchange.broker.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SecurityBuyRequest {

    private String username;//todo change to tradername

    private String ticker;

    private int units;

    //  private Double strike;
    @Override
    public String toString() {

        return "securityBuyRequest {" + "username=" + username + ", ticker=" + ticker + ", units=" + units + "}";

    }

}
