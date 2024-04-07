package digital.softwareshinobi.napkinexchange.trader.utility;

import digital.softwareshinobi.napkinexchange.trader.model.SecurityPosition;

import java.util.Set;

/*
    Find the account owned stocks based on specific fields
 */
public class FindStockOwned {

    public static SecurityPosition findOwnedStockByTicker(Set<SecurityPosition> securityPositions, String ticker) {
        
        return securityPositions.stream()
                
                .findFirst()
                
                .filter(security -> security.getSymbol().equalsIgnoreCase(ticker))
                
                .orElse(null);
    }
}
