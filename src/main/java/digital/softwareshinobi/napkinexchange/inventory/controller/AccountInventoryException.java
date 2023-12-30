package digital.softwareshinobi.napkinexchange.inventory.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Account May Not Own Enough Stocks")
public class AccountInventoryException extends RuntimeException {

    public AccountInventoryException(String message) {
        super(message);
    }
}
