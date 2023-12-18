package api.utils.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class PhoneAlreadyUseException extends ResponseStatusException {

    public PhoneAlreadyUseException() {
        super(HttpStatus.BAD_REQUEST, "This phone already use");
    }
}
