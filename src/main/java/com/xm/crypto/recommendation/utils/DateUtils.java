package com.xm.crypto.recommendation.utils;

import com.xm.crypto.recommendation.exception.ValidationException;
import com.xm.crypto.recommendation.model.DateRange;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Optional;

public class DateUtils {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static LocalDate getDatefromString(String inputDate) {
        try {
            return LocalDate.parse(inputDate, DATE_FORMATTER);
        } catch (DateTimeParseException e) {
            throw new ValidationException("Invalid date format. Please use yyyy-MM-dd.");
        }
    }

    public static Optional<DateRange> getTimeRangeFromString(String inputStartDate, String inputEndDate) {
        if(StringUtils.isEmpty(inputStartDate) && StringUtils.isEmpty(inputEndDate))
            return Optional.empty();

        if ((StringUtils.isNotEmpty(inputStartDate) && StringUtils.isEmpty(inputEndDate)) ||
                StringUtils.isEmpty(inputStartDate) && StringUtils.isNotEmpty(inputEndDate)) {
            throw new ValidationException("A valid time range needs to be provided!");
        }

        LocalDate startDate = getDatefromString(inputStartDate);
        LocalDate endDate = getDatefromString(inputEndDate);

        return Optional.of(new DateRange(startDate, endDate));
    }
}
