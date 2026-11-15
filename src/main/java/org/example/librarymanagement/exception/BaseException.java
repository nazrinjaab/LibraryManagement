package org.example.librarymanagement.exception;

import lombok.Getter;

@Getter
public abstract class BaseException extends RuntimeException {

    private final String messageKey;
    private final Object[] args;

    protected BaseException(String messageKey, Object... args) {
        super(messageKey);
        this.messageKey = messageKey;
        this.args = args;
    }
}
