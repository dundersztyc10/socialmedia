package pl.dundersztyc.common;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler({IllegalArgumentException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    void handleIllegalArgumentException() {
    }

    @ExceptionHandler({IllegalStateException.class})
    @ResponseStatus(HttpStatus.FORBIDDEN)
    void handleIllegalStateException() {
    }

    @ExceptionHandler({EntityNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    void handleEntityNotFoundException() {
    }


}
