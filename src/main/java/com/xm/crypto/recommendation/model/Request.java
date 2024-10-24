package com.xm.crypto.recommendation.model;

import java.util.Optional;

public class Request {
    private String symbol;
    private String startDate;
    private String endDate;
    private String forDate;



    private Request(String symbol, String startDate, String endDate, String forDate) {
        this.symbol = symbol;
        this.startDate = startDate;
        this.endDate = endDate;
        this.forDate = forDate;
    }

    public static Request.Builder builder(){
        return new Builder();
    }


    public static class Builder {
        private String symbol;
        private String startDate;
        private String endDate;
        private String forDate;

        private Builder() {
        }

        public Builder symbol(String symbol) {
            this.symbol = symbol;
            return this;
        }

        public Builder startDate(String startDate) {
            this.startDate = startDate;
            return this;
        }

        public Builder endDate(String endDate) {
            this.endDate = endDate;
            return this;
        }
        public Builder forDate(String forDate) {
            this.forDate = forDate;
            return this;
        }


        public Request build() {
            return new Request(symbol, startDate, endDate, forDate);
        }
    }

    public String getSymbol() {
        return symbol;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public String getForDate() {
        return forDate;
    }
}