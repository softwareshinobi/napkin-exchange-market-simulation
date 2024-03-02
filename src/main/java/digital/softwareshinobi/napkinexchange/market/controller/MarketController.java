package digital.softwareshinobi.napkinexchange.market.controller;

import digital.softwareshinobi.napkinexchange.market.entity.Market;
import digital.softwareshinobi.napkinexchange.market.service.MarketService;
import java.time.ZonedDateTime;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping(value = "market")
public class MarketController {

    @Autowired
    private final MarketService marketService;

    @RequestMapping()
    public Market findMarketEntity() {

        return marketService.findMarketEntity();

    }

    @GetMapping(value = "time")
    public ZonedDateTime getSystemTime() {

        return marketService.systemTime();

    }

}
