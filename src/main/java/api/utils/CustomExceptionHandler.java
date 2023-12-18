package api.utils;

import api.utils.exceptions.AddressAlreadyUsedException;
import api.utils.exceptions.EntityNotFoundException;
import api.utils.exceptions.PhoneAlreadyUseException;
import api.utils.exceptions.TitleAlreadyUsedException;
import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseError handle(ValidationException e) {
        log.error(e.getMessage(), e);
        return new ResponseError(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseError handle(EntityNotFoundException e) {
        log.error(e.getMessage(), e);
        return new ResponseError(HttpStatus.NOT_FOUND, e.getReason());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseError handle(PhoneAlreadyUseException e) {
        log.error(e.getMessage(), e);
        return new ResponseError(HttpStatus.BAD_REQUEST, e.getReason());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseError handle(AddressAlreadyUsedException e) {
        log.error(e.getMessage(), e);
        return new ResponseError(HttpStatus.BAD_REQUEST, e.getReason());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseError handle(TitleAlreadyUsedException e) {
        log.error(e.getMessage(), e);
        return new ResponseError(HttpStatus.BAD_REQUEST, e.getReason());
    }

}
