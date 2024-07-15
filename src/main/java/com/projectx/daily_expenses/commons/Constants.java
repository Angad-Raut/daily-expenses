package com.projectx.daily_expenses.commons;

import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;

@Component
public final class Constants {
    public static final String EXPENSE_NOT_EXISTS="Expense details not present in the system!!";
    public static final String ERROR_MSG="Expense details should not be insert or update due to some technical issue!!";
    public static final String EXPENSE_EXISTS="Expense details already added for today!!";
    public static final String EXPENSE_ITEM_EXISTS="Expense item already added please try another!!";
    public static final String MOBILE_NUMBER_EXISTS="Mobile number already exist!!";
    public static final String EMAIL_ID_EXISTS="Email Id already exist!!";
    public static final String USER_NOT_EXISTS="Please login with valid user!!";
    public static final String INVALID_CREDENTIALS="Invalid username or password!!";
    public static final String DASH="-";
    public static final String OPEN="Open";
    public static final String CLOSE="Closed";
    public static final String ISO_DATE_FORMAT="yyyy-MM-dd'T'HH:mm:ssZ";
    public static final String VIEW_DATE_FORMAT="dd MMMM yyyy";
    public static final String toExpenseDate(Date expenseDate) {
        SimpleDateFormat format = new SimpleDateFormat(VIEW_DATE_FORMAT);
        return format.format(expenseDate);
    }
    public static Date atStartOfDay() {
        Date date = new Date();
        LocalDateTime localDateTime = dateToLocalDateTime(date);
        LocalDateTime startOfDay = localDateTime.with(LocalTime.MIN);
        return localDateTimeToDate(startOfDay);
    }

    public static Date atEndOfDay() {
        Date date = new Date();
        LocalDateTime localDateTime = dateToLocalDateTime(date);
        LocalDateTime endOfDay = localDateTime.with(LocalTime.MAX);
        return localDateTimeToDate(endOfDay);
    }
    public static Date firstDayOfMonth() {
        Date currentDay = new Date();
        LocalDateTime firstDayOfMonth = dateToLocalDateTime(currentDay).with(TemporalAdjusters.firstDayOfMonth()).with(LocalTime.MIN);
        return localDateTimeToDate(firstDayOfMonth);
    }
    public static Date lastDayOfMonth() {
        Date currentDay = new Date();
        LocalDateTime lastDayOfMonth = dateToLocalDateTime(currentDay).with(TemporalAdjusters.lastDayOfMonth()).with(LocalTime.MAX);
        return localDateTimeToDate(lastDayOfMonth);
    }
    private static LocalDateTime dateToLocalDateTime(Date date) {
        return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
    }

    private static Date localDateTimeToDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }
    public static Date getISOStartDate(String startDate) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat(VIEW_DATE_FORMAT);
        Date beforeDate = format.parse(startDate);
        SimpleDateFormat ISOFormat = new SimpleDateFormat(ISO_DATE_FORMAT);
        String convertedDate = ISOFormat.format(beforeDate);
        Date finalDate = ISOFormat.parse(convertedDate);
        return localDateTimeToDate(dateToLocalDateTime(finalDate).with(LocalTime.MIN));
    }
    public static Date getISOEndDate(String endDate) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat(VIEW_DATE_FORMAT);
        Date beforeDate = format.parse(endDate);
        SimpleDateFormat ISOFormat = new SimpleDateFormat(ISO_DATE_FORMAT);
        String convertedDate = ISOFormat.format(beforeDate);
        Date finalDate = ISOFormat.parse(convertedDate);
        return localDateTimeToDate(dateToLocalDateTime(finalDate).with(LocalTime.MAX));
    }
}
