package ru.practicum.mainservice.exception;

public class LimitRequestException extends RuntimeException {

    public LimitRequestException(String message) {
        super(message);
    }
}
