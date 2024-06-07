package digital.softwareshinobi.napkinexchange.trader.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Invalid account balance for transaction")
public class TraderBalanceException extends RuntimeException {

    public TraderBalanceException(String message) {

        super(message);

    }

}
