package digital.softwareshinobi.napkinexchange.market.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import digital.softwareshinobi.napkinexchange.market.enums.MarketTrajectory;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.time.ZonedDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Market {

    @Id
    @JsonIgnore
    private final Integer id = 1;

    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private ZonedDateTime date;

    @Column
    private Double lastMonthAveragePrice;

    @Column
    @Enumerated(EnumType.STRING)
    private MarketTrajectory marketTrajectory;

    public void tick() {

        ZonedDateTime newDate = this.getDate().plusMinutes(1);

        this.setDate(newDate);

    }

}
