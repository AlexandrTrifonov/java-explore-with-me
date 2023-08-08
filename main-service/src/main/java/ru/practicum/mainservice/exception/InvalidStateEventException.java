package ru.practicum.mainservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class InvalidStateEventException extends RuntimeException {

    public InvalidStateEventException(String message) {
        super(message);
    }
}
