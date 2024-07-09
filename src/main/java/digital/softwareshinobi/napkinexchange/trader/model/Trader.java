package digital.softwareshinobi.napkinexchange.trader.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import digital.softwareshinobi.napkinexchange.trader.portfolio.SecurityPosition;
import digital.softwareshinobi.napkinexchange.trader.utility.CalculateCostBasisAndProfits;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.Set;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "trader")
@Getter
@Setter
@NoArgsConstructor
public class Trader implements Serializable {

    @Id
    private String username;

    @JsonIgnore
    @Column(name = "password")
    private String password;

    @Column(name = "leverage")
    private Double leverage = 50.0;

    @Column(name = "available_funds")
    private Double availableFunds;

    @OneToMany(mappedBy = "trader", fetch = FetchType.EAGER)
    @JsonManagedReference
    private Set<SecurityPosition> securityPortfolio;

    @JsonIgnore
    @OneToMany(mappedBy = "account", fetch = FetchType.EAGER)
    @JsonManagedReference
    private List<AccountHistory> accountHistory;

    public Trader(String username, String password) {

        this.username = username;

        this.password = password;

        this.availableFunds = 0.0;

        this.leverage = 50.0;

    }

    public Double getTotalAccountValue() {
        return this.availableFunds + this.getPortfolioValue();
    }   

    public Double getPortfolioValue() {

        double portfolioValue = 0;

        if(this.securityPortfolio==null)return 0.0;
        
        for (SecurityPosition securityPosition : this.securityPortfolio) {

            portfolioValue = portfolioValue +securityPosition.getValue();

        }

        return portfolioValue;

    }

    public Double getFundsUtilizationPercentage() {
        return this.getPortfolioValue() / this.getTotalAccountValue();
    }

    @Override
    public String toString() {

        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(" { ");

        stringBuilder.append("trader").append(" : ").append(this.username).append(", ");

        stringBuilder.append("totalAccountValue").append(" : ").append(this.getTotalAccountValue()).append(", ");

        stringBuilder.append("availableFunds").append(" : ").append(this.availableFunds).append(", ");

        stringBuilder.append("portfolioValue").append(" : ").append(this.getPortfolioValue()).append(", ");

        stringBuilder.append("utilization").append(" : ").append(this.getFundsUtilizationPercentage()).append(", ");

        stringBuilder.append("portfolio").append(" : ").append(this.securityPortfolio).append(", ");

        stringBuilder.append(" } ");

        String toString = stringBuilder.toString();

        stringBuilder = null;

        return toString;

    }

}
