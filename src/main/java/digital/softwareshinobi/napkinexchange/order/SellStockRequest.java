package digital.softwareshinobi.napkinexchange.order;

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

    private String ticker;

    private int sharesToSell;

}
