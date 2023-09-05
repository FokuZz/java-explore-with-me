package ru.practicum.explore.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import ru.practicum.explore.model.ErrorResponse;

import javax.validation.ValidationException;
import java.time.format.DateTimeParseException;

@RestControllerAdvice
@Slf4j
public class ErrorHandler {

    @ExceptionHandler({IllegalArgumentException.class, DateTimeParseException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleIllegalArgumentException(final RuntimeException e) {
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleMissingServletRequestParameterException(final MissingServletRequestParameterException e) {
        log.error(e.getMessage(), e);
        return new ErrorResponse("Required request parameter is not present: " + e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidException(final MethodArgumentNotValidException e) {
        log.error(e.getMessage(), e);
        return new ErrorResponse("Validation error: " + e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidException(final MethodArgumentTypeMismatchException e) {
        log.error(e.getMessage(), e);
        return new ErrorResponse("Wrong type of value: " + e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidationException(final ValidationException e) {
        log.error(e.getMessage(), e);
        return new ErrorResponse("Incorrectly made request: " + e.getMessage());
    }


    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNoHandlerFoundException(final NoHandlerFoundException e, WebRequest request) {
        log.error("Неизвестный запрос: {}", request.getHeaderNames());
        return new ErrorResponse("The required object was not found: " + e.getMessage());

    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleThrowable(final Throwable e) {
        log.error("Произошла непредвиденная ошибка: " + e.getMessage(), e);
        return new ErrorResponse("An unexpected error has occurred: " + e.getClass() + ": " + e.getMessage());
    }
}
