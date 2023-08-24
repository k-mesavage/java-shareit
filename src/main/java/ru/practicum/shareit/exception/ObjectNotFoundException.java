package ru.practicum.shareit.exception;

public class ObjectNotFoundException extends RuntimeException {

    private final String message;

    public ObjectNotFoundException(String message) {
        this.message = message;
    }
}
