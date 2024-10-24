package com.xm.crypto.recommendation.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Crypto {
    private String symbol;
    private LocalDateTime timestamp;
    private BigDecimal price;

    public Crypto(String symbol, LocalDateTime timestamp, BigDecimal price) {
        this.symbol = symbol;
        this.timestamp = timestamp;
        this.price = price;
    }

    public String getSymbol() {
        return symbol;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public BigDecimal getPrice() {
        return price;
    }
}
