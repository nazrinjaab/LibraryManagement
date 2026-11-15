package org.example.librarymanagement.exception;

public class BusinessException extends BaseException {

    public BusinessException(String messageKey, Object... args) {
        super(messageKey, args);
    }
}