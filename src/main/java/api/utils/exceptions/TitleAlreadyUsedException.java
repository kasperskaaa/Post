package api.utils.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class TitleAlreadyUsedException extends ResponseStatusException {
    public TitleAlreadyUsedException() {
        super(HttpStatus.BAD_REQUEST, "This title is already used");
    }
}
