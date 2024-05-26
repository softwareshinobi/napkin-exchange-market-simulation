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
@Table(name = "accounts")
@Getter
@Setter
@NoArgsConstructor
public class Trader implements Serializable {

    @Id
    private String username;

    @JsonIgnore
    @Column(name = "pin")
    private String password;

    @Column(name = "leverage")
    private Double leverage = 50.0;

    @Column(name = "balance")
    private Double accountBalance;

    @JsonIgnore
    @Column(name = "profit_loss")
    private Double totalProfits;

    @OneToMany(mappedBy = "trader", fetch = FetchType.EAGER)
    @JsonManagedReference
    private Set<SecurityPosition> securityPortfolio;

    
    @Column(name = "portfolioValue")
    private Double portfolioValue;

    @Column(name = "accountValue")
private Double accountValue=0.0;

        @Column(name = "utilizationPercentage")
    private Double utilizationPercentage=0.0;
    
    @JsonIgnore
    @OneToMany(mappedBy = "account", fetch = FetchType.EAGER)
    @JsonManagedReference
    private List<AccountHistory> accountHistory;

    public Trader(String username, String password) {

        this.username = username;

        this.password = password;

        this.accountBalance = 0.0;

        this.portfolioValue=0.0;
        
        this.totalProfits = 0.0;

        this.leverage = 50.0;

    }

    public void updateTotalProfits(double costBasis, int sharesToSell, double currentPrice) {

        if (this.totalProfits == null) {

            this.totalProfits = 0.0;

        }

        double updatedTotalProfits = CalculateCostBasisAndProfits.findProfitsAfterSelling(
                this.totalProfits,
                costBasis,
                sharesToSell,
                currentPrice);
        
        System.out.println("updatedTotalProfits / "+updatedTotalProfits);
        
        this.setTotalProfits(updatedTotalProfits);

    }

    public void updateAccountBalance(double amountToAdd) {

//        if (this.getAccountBalance() + amountToAdd < 0) {
//
//            throw new AccountBalanceException("Must have more money in account");
//
//        }
        this.setAccountBalance(Math.round((this.getAccountBalance() + amountToAdd) * 100.00) / 100.00);

    }

//    public Double calculateSecurityPortfolioValue(){
//        
//        double val=0;
//        
//        for(SecurityPosition securityPosition:this.securityPortfolio){
//        
//            val = val +securityPosition.value;
//            
//        }
//        return  val;
//        
//    }
    @Override
    public String toString() {

        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(" { ");

        stringBuilder.append("username").append(" : ").append(username).append(", ");

        stringBuilder.append("accountBalance").append(" : ").append(accountBalance).append(", ");

        stringBuilder.append("totalProfits").append(" : ").append(totalProfits).append(", ");

        stringBuilder.append("stocksOwned").append(" : ").append(securityPortfolio).append(", ");

        stringBuilder.append(" } ");

        String toString = stringBuilder.toString();

        stringBuilder = null;

        return toString;

    }

}
