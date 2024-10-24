package com.xm.crypto.recommendation.model;

public class ErrorDetails {

    private final String errorType;
    private final String errorMessage;

    public ErrorDetails(String errorType, String errorMessage) {
        this.errorType = errorType;
        this.errorMessage = errorMessage;
    }

    public String getErrorType() {
        return errorType;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
