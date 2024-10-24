package com.xm.crypto.recommendation.model;

import java.math.BigDecimal;

public class CryptoNormalizedRange {
    private String symbol;
    private BigDecimal normalizedRange;

    public CryptoNormalizedRange(String symbol, BigDecimal normalizedRange) {
        this.symbol = symbol;
        this.normalizedRange = normalizedRange;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public BigDecimal getNormalizedRange() {
        return normalizedRange;
    }

    public void setNormalizedRange(BigDecimal normalizedRange) {
        this.normalizedRange = normalizedRange;
    }
}