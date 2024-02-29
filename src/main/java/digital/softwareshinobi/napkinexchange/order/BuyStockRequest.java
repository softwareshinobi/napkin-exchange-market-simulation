package digital.softwareshinobi.napkinexchange.order;

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
