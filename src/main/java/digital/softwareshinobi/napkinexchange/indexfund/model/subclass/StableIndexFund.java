package digital.softwareshinobi.napkinexchange.indexfund.model.subclass;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import digital.softwareshinobi.napkinexchange.indexfund.enums.FundTracking;
import digital.softwareshinobi.napkinexchange.indexfund.model.IndexFund;

import jakarta.persistence.*;

@Entity
@Table(name = "stable_index_fund")
@NoArgsConstructor
@Getter
@Setter
public class StableIndexFund extends IndexFund {

    @Column(name = "volatility")
    private Boolean volatility;

    public StableIndexFund(double price) {
        super("Stable Index Fund", price, FundTracking.VOLATILITY);
        this.volatility = false;
    }
}
