package ru.practicum.mainservice.exception;

public class InvalidUniqueKeyException extends RuntimeException {

    public InvalidUniqueKeyException(String message) {
        super(message);
    }

}
