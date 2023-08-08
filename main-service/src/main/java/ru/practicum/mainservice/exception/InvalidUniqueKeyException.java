package ru.practicum.mainservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class InvalidUniqueKeyException extends RuntimeException {

    public InvalidUniqueKeyException(String message) {
        super(message);
    }

}
