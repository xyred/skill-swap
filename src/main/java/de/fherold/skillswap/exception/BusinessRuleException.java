package de.fherold.skillswap.exception;

import lombok.Getter;

@Getter
public class BusinessRuleException extends RuntimeException {
    private final String errorCode;

    public BusinessRuleException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
}
