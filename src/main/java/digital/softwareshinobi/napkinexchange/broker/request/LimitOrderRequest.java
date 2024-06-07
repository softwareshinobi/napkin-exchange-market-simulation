package digital.softwareshinobi.napkinexchange.broker.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class LimitOrderRequest {

    private String trader;

    private String ticker;

    private int units;

    private double strike;

}
