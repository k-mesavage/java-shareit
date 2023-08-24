package ru.practicum.shareit.exception;

public class EmailAlreadyException extends RuntimeException {

    public EmailAlreadyException(String message) {
        super(message);
    }
}
