package com.ing.hub.loan.domain.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.YearMonth;

public final class Utils {
    private Utils() {}

    public static LocalDate firstDayOfNextMonth(LocalDate date) {
        YearMonth ym = YearMonth.from(date).plusMonths(1);
        return ym.atDay(1);
    }

    public static LocalDate firstDayOfMonth(LocalDate date) {
        YearMonth ym = YearMonth.from(date);
        return ym.atDay(1);
    }

    public static BigDecimal bd(double v) {
        return BigDecimal.valueOf(v).setScale(2, RoundingMode.HALF_UP);
    }

    public static BigDecimal scale2(BigDecimal v) {
        return v.setScale(2, RoundingMode.HALF_UP);
    }
}
