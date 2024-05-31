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

    private String username;

    private String ticker;

    private int units;

    @Override
    public String toString() {

        return "{" + "username=" + username + ", ticker=" + ticker + ", units=" + units + "}";

    }

}
