package org.example.librarymanagement.exception;

import lombok.RequiredArgsConstructor;
import org.example.librarymanagement.dto.response.ErrorResponse;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final MessageSource messageSource;

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFound(NotFoundException ex) {
        return new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                resolveMessage(ex),
                null,
                LocalDateTime.now()
        );
    }

    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleBusiness(BusinessException ex){
        return new ErrorResponse(
                HttpStatus.CONFLICT.value(),
                resolveMessage(ex),
                null,
                LocalDateTime.now()
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidation(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors()
                .forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));

        String message = messageSource.getMessage(
                "error.validation", null, LocaleContextHolder.getLocale()
        );

        return new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                message,
                errors,
                LocalDateTime.now()
        );
    }

    private String resolveMessage(BaseException ex){
        Object[] args = Arrays.stream(ex.getArgs())
                .map(String::valueOf)
                .toArray();

        return messageSource.getMessage(
                ex.getMessageKey(),
                args,
                ex.getMessageKey(),
                LocaleContextHolder.getLocale()
        );
    }

}
