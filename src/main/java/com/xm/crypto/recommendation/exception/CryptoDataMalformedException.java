package com.xm.crypto.recommendation.exception;

public class CryptoDataMalformedException extends RuntimeException {
    public CryptoDataMalformedException(String error, Exception e) {
        super(error, e);
    }

    public CryptoDataMalformedException(String error) {
        super(error);
    }
}
