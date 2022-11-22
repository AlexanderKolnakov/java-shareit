package ru.practicum.shareit.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code= HttpStatus.CONFLICT)
public class UserCreateException extends RuntimeException {

    public UserCreateException(final String message) {
        super(message);
    }

}