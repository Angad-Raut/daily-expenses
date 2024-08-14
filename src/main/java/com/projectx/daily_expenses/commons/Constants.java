package com.projectx.daily_expenses.commons;

import org.springframework.stereotype.Component;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;
import java.util.Locale;

@Component
public final class Constants {
    public static final String EXPENSE_NOT_EXISTS="Expense details not present in the system!!";
    public static final String ERROR_MSG="Expense details should not be insert or update due to some technical issue!!";
    public static final String EXPENSE_EXISTS="Expense details already added for today!!";
    public static final String EXPENSE_ITEM_EXISTS="Expense item already added please try another!!";
    public static final String MOBILE_NUMBER_EXISTS="Mobile number already exist!!";
    public static final String EMAIL_ID_EXISTS="Email Id already exist!!";
    public static final String USER_NOT_EXISTS="Please login with valid user!!";
    public static final String USER_PROFILE_NOT_EXISTS="User profile details not present in the system!!";
    public static final String PAN_NUMBER_ALREADY_EXISTS="Pan card number already exists!!";
    public static final String AADHAR_NUMBER_ALREADY_EXISTS="Aadhar card number already exists!!";
    public static final String OLD_PASSWORD_NOT_MATCH="Old password doesn't match with system password!!";
    public static final String NEW_PASSWORD_SAME="New password should not be same as old password!!";
    public static final String INVALID_CREDENTIALS="Invalid username or password!!";
    public static final String DOCUMENT_ALREADY_EXIST="This document already exists in the system!!";
    public static final String DOCUMENT_NOT_FOUND="Document not present in the system!!";
    public static final String INCOME_DETAILS_NOT_EXISTS="Income details not present in the system!!";
    public static final String INCOME_DETAILS_ALREADY_EXISTS="Income details already exists in the system!!";
    public static final String INCOME_TYPE_NOT_CHANGED="Income type can not be change!!";
    public static final String COMPANY_DETAILS_NOT_EXISTS="Company details not present!!";
    public static final String COMPANY_DOCUMENT_NOT_EXISTS="Company document not present!!";
    public static final String COMPANY_DETAILS_ALREADY_EXISTS="Company details already exists!!";
    public static final String DASH="-";
    public static final String OPEN="Open";
    public static final String CLOSE="Closed";
    public static final String SALARY_TYPE="SALARY";
    public static final String FORM_16_TYPE="FORM_16";
    public static final String OFFER_LETTER_TYPE="OFFER_LETTER";
    public static final String EXPERIENCE_LETTER_TYPE="EXPERIENCE_LETTER";
    public static final String SERVICE_LETTER_TYPE="SERVICE_LETTER";
    public static final String APPOINTMENT_LETTER_TYPE="APPOINTMENT_LETTER";
    public static final String OTHER_TYPE="OTHER";
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
    public static Date firstDayOfYear() {
        Date currentDay = new Date();
        LocalDateTime firstDayOfYear = dateToLocalDateTime(currentDay).with(TemporalAdjusters.firstDayOfYear()).with(LocalTime.MIN);
        return localDateTimeToDate(firstDayOfYear);
    }
    public static Date lastDayOfMonth() {
        Date currentDay = new Date();
        LocalDateTime lastDayOfMonth = dateToLocalDateTime(currentDay).with(TemporalAdjusters.lastDayOfMonth()).with(LocalTime.MAX);
        return localDateTimeToDate(lastDayOfMonth);
    }
    public static Date lastDayOfYear() {
        Date currentDay = new Date();
        LocalDateTime lastDayOfYear = dateToLocalDateTime(currentDay).with(TemporalAdjusters.lastDayOfYear()).with(LocalTime.MAX);
        return localDateTimeToDate(lastDayOfYear);
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
    public static Date getISODate(String date) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat(VIEW_DATE_FORMAT);
        Date beforeDate = format.parse(date);
        SimpleDateFormat ISOFormat = new SimpleDateFormat(ISO_DATE_FORMAT);
        String convertedDate = ISOFormat.format(beforeDate);
        Date finalDate = ISOFormat.parse(convertedDate);
        return localDateTimeToDate(dateToLocalDateTime(finalDate));
    }
    public static Date getISOEndDate(String endDate) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat(VIEW_DATE_FORMAT);
        Date beforeDate = format.parse(endDate);
        SimpleDateFormat ISOFormat = new SimpleDateFormat(ISO_DATE_FORMAT);
        String convertedDate = ISOFormat.format(beforeDate);
        Date finalDate = ISOFormat.parse(convertedDate);
        return localDateTimeToDate(dateToLocalDateTime(finalDate).with(LocalTime.MAX));
    }
    public static String toINRFormat(Double amount) {
        NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("en", "IN"));
        format.setCurrency(java.util.Currency.getInstance("INR"));
        String formattedAmount = format.format(amount);
        return formattedAmount;
    }
}
