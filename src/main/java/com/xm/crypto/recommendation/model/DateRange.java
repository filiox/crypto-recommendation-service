package com.xm.crypto.recommendation.model;

import com.xm.crypto.recommendation.exception.ValidationException;
import java.time.LocalDate;

public class DateRange {
    private LocalDate start;
    private LocalDate end;

    public DateRange(LocalDate start, LocalDate end) {
        this.start = start;
        this.end = end;
        if (start.isAfter(end)) {
            throw new ValidationException("Start date must be before end date!");
        }
    }

    public LocalDate getStart() {
        return start;
    }

    public LocalDate getEnd() {
        return end;
    }

    @Override
    public String toString() {
        return "DateRange{" +
                "start=" + start +
                ", end=" + end +
                '}';
    }
}
