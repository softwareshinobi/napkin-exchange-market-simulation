package digital.softwareshinobi.napkinexchange.leaderboard.model;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Leaderboard {

    private final UUID id = UUID.randomUUID();

    private Integer ranking;

    private String username;

    private Double cashBalance;

    private Double accountNetWorth;

}
