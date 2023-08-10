package ru.practicum.mainservice.exception;

public class InvalidEventDateException extends RuntimeException {

    public InvalidEventDateException(String message) {
        super(message);
    }
}
