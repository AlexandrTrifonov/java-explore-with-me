package ru.practicum.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class InvalidDateException extends RuntimeException {

    public InvalidDateException(String message) {
        super(message);
    }
}