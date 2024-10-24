package com.xm.crypto.recommendation.model;

import java.math.BigDecimal;

public class CryptoStats {
    private BigDecimal oldestPrice;
    private BigDecimal newestPrice;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;

    public CryptoStats(BigDecimal oldestPrice, BigDecimal newestPrice, BigDecimal minPrice, BigDecimal maxPrice) {
        this.oldestPrice = oldestPrice;
        this.newestPrice = newestPrice;
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
    }

    public BigDecimal getOldestPrice() {
        return oldestPrice;
    }

    public BigDecimal getNewestPrice() {
        return newestPrice;
    }

    public BigDecimal getMinPrice() {
        return minPrice;
    }

    public BigDecimal getMaxPrice() {
        return maxPrice;
    }
}
