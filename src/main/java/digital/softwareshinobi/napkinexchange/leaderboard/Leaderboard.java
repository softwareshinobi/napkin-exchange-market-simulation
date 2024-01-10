package digital.softwareshinobi.napkinexchange.leaderboard;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Leaderboard {

    private final UUID id = UUID.randomUUID();

    private int ranking;

    private String username;

    private double totalProfits;

}
