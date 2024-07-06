package digital.softwareshinobi.napkinexchange.leaderboard.controller;

import digital.softwareshinobi.napkinexchange.leaderboard.model.Leaderboard;
import digital.softwareshinobi.napkinexchange.leaderboard.service.LeaderboardService;
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
    private final LeaderboardService leaderboardService;

    @RequestMapping(value = "")
    public List<Leaderboard> getLeaderboardTopTen() {

        return this.leaderboardService.topTenTraders();

    }

    @RequestMapping(value = "{username}")
    public Leaderboard getLeaderboardByUsername(@PathVariable String username) {

        return this.leaderboardService.findByTraderName(username);

    }

}
