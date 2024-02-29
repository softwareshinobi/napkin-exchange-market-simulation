package digital.softwareshinobi.napkinexchange.leaderboard;

import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@AllArgsConstructor
@RequestMapping(value = "leaderboard")
public class LeaderboardController {

    @Autowired
    private final ProcessLeaderboard processLeaderboard;

    @RequestMapping(value = "/")
    public List<Leaderboard> getLeaderboardTopTen() {

        return processLeaderboard.topTenAccounts();

    }

    @RequestMapping(value = "/podium")
    public List<Leaderboard> getLeaderboardPodium() {

        return processLeaderboard.topThreeAccounts();

    }

    @RequestMapping(value = "/rank/{username}")
    public Leaderboard getLeaderboardByUsername(@PathVariable String username) {

        return processLeaderboard.findAccountRanking(username);

    }

}
