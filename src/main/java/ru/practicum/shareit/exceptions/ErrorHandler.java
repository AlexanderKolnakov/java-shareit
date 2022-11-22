package ru.practicum.shareit.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(value = HttpStatus.CONFLICT)
    public String error409(final UserCreateException e) {
        log.info("409 {}", e.getMessage());
        return e.getMessage();
    }

    @ExceptionHandler
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public String error400(final MethodArgumentNotValidException e) {
        log.info("400 {}", e.getMessage());
        return e.getFieldError().getDefaultMessage();
    }
    @ExceptionHandler
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public String error400(final MissingRequestHeaderException e) {
        log.info("400 {}", e.getMessage());
        return e.getMessage();
    }

    @ExceptionHandler
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public String error404(final UserNotFoundException e) {
        log.info("404 {}", e.getMessage());
        return e.getMessage();
    }
}
