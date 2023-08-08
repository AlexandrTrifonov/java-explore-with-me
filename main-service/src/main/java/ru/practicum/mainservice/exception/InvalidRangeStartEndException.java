package ru.practicum.mainservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidRangeStartEndException extends RuntimeException {

    public InvalidRangeStartEndException(String message) {
        super(message);
    }
}
