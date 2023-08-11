package ru.practicum.mainservice.exception;

public class InvalidRangeStartEndException extends RuntimeException {

    public InvalidRangeStartEndException(String message) {
        super(message);
    }
}
