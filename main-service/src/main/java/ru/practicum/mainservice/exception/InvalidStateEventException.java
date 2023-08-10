package ru.practicum.mainservice.exception;

public class InvalidStateEventException extends RuntimeException {

    public InvalidStateEventException(String message) {
        super(message);
    }
}
