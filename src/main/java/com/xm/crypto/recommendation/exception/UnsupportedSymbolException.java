package com.xm.crypto.recommendation.exception;

public class UnsupportedSymbolException extends RuntimeException {
    public UnsupportedSymbolException(String symbol) {
        super(String.format("Unsupported symbol %s", symbol));
    }
}
