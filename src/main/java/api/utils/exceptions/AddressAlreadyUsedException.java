package api.utils.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class AddressAlreadyUsedException extends ResponseStatusException {
    public AddressAlreadyUsedException() {
        super(HttpStatus.BAD_REQUEST, "This address is already used");
    }
}
