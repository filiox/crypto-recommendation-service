package com.xm.crypto.recommendation.exception;

public class ValidationException extends RuntimeException {
    public ValidationException(String error) {
        super(error);
    }
}
