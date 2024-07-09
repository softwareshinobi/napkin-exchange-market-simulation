package digital.softwareshinobi.napkinexchange.security.controller;

import digital.softwareshinobi.napkinexchange.security.exception.SecurityNotFoundException;
import digital.softwareshinobi.napkinexchange.security.model.Security;
import digital.softwareshinobi.napkinexchange.security.model.SecurityPricingHistory;
import digital.softwareshinobi.napkinexchange.security.service.SecurityHistoryService;
import digital.softwareshinobi.napkinexchange.security.service.SecurityService;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "security")
@CrossOrigin
@AllArgsConstructor
@SuppressWarnings("unused")
public class SecurityController {

    @Autowired
    private SecurityService securityService;

    @Autowired
    private SecurityHistoryService securityHistoryService;

    @GetMapping(value = "")
    public List<Security> fetchAllSecurities() {

        return this.securityService.getAllSecurities();

    }

    @GetMapping(value = "{ticker}")
    public Security fetchSecurityByTicker(@PathVariable String ticker) {

        return this.securityService.getSecurityByTicker(ticker);

    }

    @GetMapping(value = "/pricing/{ticker}")
    public double fetchSecurityPricingByTicker(@PathVariable String ticker) throws SecurityNotFoundException {

        return this.securityService.getSecurityByTicker(ticker).getPrice();

    }

    @GetMapping(value = "/history/{ticker}")
    public List<SecurityPricingHistory> fetchSecurityPricingHistoryByTicker(@PathVariable String ticker) {

        return this.securityHistoryService.getSecurityPricingHistoryByTicker(ticker);

    }

    @GetMapping(value = "/last/{ticker}")
    public SecurityPricingHistory fetchLastSecurityPricingHistoryByTicker(@PathVariable String ticker) {
//this is a hack

        List<SecurityPricingHistory> list = this.fetchSecurityPricingHistoryByTicker(ticker);

        return list.get(list.size() - 1);

    }

    @GetMapping(value = "health")
    public String health() {

        return "OK";

    }

}
