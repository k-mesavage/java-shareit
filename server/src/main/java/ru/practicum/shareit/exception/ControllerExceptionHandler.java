package ru.practicum.shareit.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.persistence.EntityNotFoundException;
import javax.validation.UnexpectedTypeException;

@Slf4j
@RestControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler({BadRequestException.class, MethodArgumentNotValidException.class, UnexpectedTypeException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse badRequest(RuntimeException ex) {
        log.info("Получен статус 400 Bad Request {}", ex.getMessage(), ex);
        return new ErrorResponse(String.format("Bad Request Exception \"%s\"", ex.getMessage()));
    }

    @ExceptionHandler({ObjectNotFoundException.class, EntityNotFoundException.class})
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public ErrorResponse objectNotFound(RuntimeException ex) {
        log.info("Получен статус 404 Not Found {}", ex.getMessage(), ex);
        return new ErrorResponse(String.format("\"%s\". Status 404", ex.getMessage()));
    }

    @ExceptionHandler({UnsupportedStatusException.class})
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse unsupportedStatus(RuntimeException ex) {
        log.info("Получен статус 500 {}", ex.getMessage(), ex);
        return new ErrorResponse("Unknown state: " + ex.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleThrowable(final Throwable ex) {
        log.info("Произошла непредвиденная ошибка: {}", ex.getMessage(), ex);
        return new ErrorResponse("Произошла непредвиденная ошибка" + ex.getMessage());
    }
}
