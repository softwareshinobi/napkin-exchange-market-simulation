package digital.softwareshinobi.napkinexchange.trader.model.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BuyStockRequest {

    private String username;
    
    private String ticker;
    
    private int sharesToBuy;

    @Override
    public String toString() {
        
        return "BuyStockRequest{" + "username=" + username + ", ticker=" + ticker + ", sharesToBuy=" + sharesToBuy + '}';
        
    }
    
}
