package com.DTISE.ShelfMasterBE.common.tools;

import java.time.*;

public class DateConverter {

    public static OffsetDateTime getStartOfWeek(LocalDate date, int weeksAgo) {
        LocalDate startOfWeek = date.with(DayOfWeek.MONDAY).minusWeeks(weeksAgo);
        return startOfWeek.atStartOfDay().atOffset(ZoneOffset.UTC);
    }

    public static OffsetDateTime getStartOfMonth(LocalDate date, int monthsAgo) {
        LocalDate startOfMonth = date.withDayOfMonth(1).minusMonths(monthsAgo);
        return startOfMonth.atStartOfDay().atOffset(ZoneOffset.UTC);
    }

    public static OffsetDateTime getStartOfDay(LocalDate date) {
        return date.atStartOfDay(ZoneOffset.UTC).toOffsetDateTime();
    }

    public static OffsetDateTime getEndOfDay(LocalDate date) {
        return date.atTime(LocalTime.MAX).atOffset(ZoneOffset.UTC);
    }

}
