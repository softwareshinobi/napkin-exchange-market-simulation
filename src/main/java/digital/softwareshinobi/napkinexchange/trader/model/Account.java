package digital.softwareshinobi.napkinexchange.trader.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
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
public class Account implements Serializable {

    @Id
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "leverage")
    private final Double leverage = 50.0;

    @Column(name = "balance")
    private Double accountBalance;

    @Column(name = "total_profits")
    private Double totalProfits;

    @OneToMany(mappedBy = "account", fetch = FetchType.EAGER)
    @JsonManagedReference
    private Set<StockOwned> stocksOwned;

    @OneToMany(mappedBy = "account", fetch = FetchType.EAGER)
    @JsonManagedReference
    private List<AccountHistory> accountHistory;

    public Account(String username, String password) {

        this.username = username;

        this.password = password;

        this.accountBalance = 0.0;

        this.totalProfits = 0.0;

    }

    public void updateTotalProfits(double costBasis, int sharesToSell, double currentPrice) {

        if (this.totalProfits == null) {

            this.totalProfits = 0.0;

        }

        setTotalProfits(CalculateCostBasisAndProfits.findProfitsAfterSelling(
                this.totalProfits, costBasis, sharesToSell, currentPrice
        ));

    }

    public void updateAccountBalance(double amountToAdd) {

//        if (this.getAccountBalance() + amountToAdd < 0) {
//
//            throw new AccountBalanceException("Must have more money in account");
//
//        }
        this.setAccountBalance(Math.round((this.getAccountBalance() + amountToAdd) * 100.00) / 100.00);

    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Account{");
        sb.append("username=").append(username);
        sb.append(", accountBalance=").append(accountBalance);
        sb.append(", totalProfits=").append(totalProfits);
        sb.append(", stocksOwned=").append(stocksOwned);
        sb.append('}');
        return sb.toString();
    }

}
