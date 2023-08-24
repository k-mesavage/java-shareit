package ru.practicum.shareit.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse badRequest(final BadRequestException ex) {
        log.info("Получен статус 400 Bad Request {}.", ex.getMessage(), ex);
        return new ErrorResponse(String.format("Bad Request Exception \"%s\".", ex.getMessage()));
    }

    @ExceptionHandler
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public ErrorResponse objectNotFound(final ObjectNotFoundException ex) {
        return new ErrorResponse(String.format("Object \"%s\" Not Found.", ex.getMessage()));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse validException(final MethodArgumentNotValidException ex) {
        log.info("Получена ошибка валидации: {}", ex.getMessage(), ex);
        return new ErrorResponse(String.format("Validation Exception \"%s\".", ex.getMessage()));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleThrowable(final Throwable ex) {
        log.info("Произошла непредвиденная ошибка: {}", ex.getMessage(), ex);
        return new ErrorResponse("Произошла непредвиденная ошибка.");
    }
}
