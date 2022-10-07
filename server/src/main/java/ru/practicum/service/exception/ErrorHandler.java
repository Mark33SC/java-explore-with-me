package ru.practicum.service.exception;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {
    @ExceptionHandler({NotFoundException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse notFoundExceptionHandle(NotFoundException e) {
        ErrorResponse response = ErrorResponse.builder()
                .message(e.getMessage())
                .reason(e.getReason())
                .status(HttpStatus.BAD_REQUEST)
                .build();

        log.warn(response.toString());

        return response;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidationException(MethodArgumentNotValidException e) {
        StringBuilder stringForLogger = new StringBuilder();

        e.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            stringForLogger.append(String.format("%s: %s; ", fieldName, errorMessage));
        });

        ErrorResponse response = ErrorResponse.builder()
                .message("Invalid data")
                .status(HttpStatus.BAD_REQUEST)
                .reason(stringForLogger.toString())
                .build();

        log.warn(response.toString());

        return response;
    }

    @ExceptionHandler({ConstraintViolationException.class})
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse constraintViolationExceptionHandle(ConstraintViolationException e) {
        ErrorResponse res = ErrorResponse.builder()
                .message(e.getMessage())
                .reason(e.getSQLException().getMessage())
                .status(HttpStatus.CONFLICT)
                .build();

        log.warn(res.toString());

        return res;
    }

    @ExceptionHandler({AlreadyExistException.class})
    @ResponseStatus(HttpStatus.ALREADY_REPORTED)
    public ErrorResponse alreadyExistExceptionHandle(AlreadyExistException e) {
        ErrorResponse res = ErrorResponse.builder()
                .message(e.getMessage())
                .reason(e.getReason())
                .status(HttpStatus.ALREADY_REPORTED)
                .build();

        log.warn(res.toString());

        return res;
    }

    @ExceptionHandler({ConflictException.class})
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse conflictExceptionHandle(ConflictException e) {
        ErrorResponse res = ErrorResponse.builder()
                .message(e.getMessage())
                .reason(e.getReason())
                .status(HttpStatus.CONFLICT)
                .build();

        log.warn(res.toString());

        return res;
    }

    @ExceptionHandler({TermNotMetException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse conditionsNotMetExceptionHandle(TermNotMetException e) {
        ErrorResponse res = ErrorResponse.builder()
                .message(e.getMessage())
                .reason(e.getReason())
                .status(HttpStatus.BAD_REQUEST)
                .build();

        log.warn(res.toString());

        return res;
    }

    @ExceptionHandler({Exception.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse internalServerErrorHandle(Exception e) {

        ErrorResponse res = ErrorResponse.builder()
                .message(e.getMessage())
                .reason("Error occurred")
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .build();

        log.warn(res.toString());

        return res;
    }
}
