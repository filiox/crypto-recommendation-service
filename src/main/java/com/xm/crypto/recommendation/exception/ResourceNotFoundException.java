package com.xm.crypto.recommendation.exception;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String error) {
        super(error);
    }
}
