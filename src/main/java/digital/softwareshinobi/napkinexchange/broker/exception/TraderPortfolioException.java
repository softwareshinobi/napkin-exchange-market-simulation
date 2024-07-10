package digital.softwareshinobi.napkinexchange.broker.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Account May Not Own Enough Stocks")
public class TraderPortfolioException extends RuntimeException {

    public TraderPortfolioException(String message) {

        super(message);

    }

}
