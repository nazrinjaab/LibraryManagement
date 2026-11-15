package org.example.librarymanagement.exception;

public class NotFoundException extends BaseException {

    public NotFoundException(String messageKey, Object... args) {
        super(messageKey, args);
    }
}