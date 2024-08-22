package com.projectx.common.utils;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.Date;

@Component
public class ResumeUtils {
    public static final String EMPLOYEE_DETAILS_NOT_FOUND="Employee details not present in the system!!";
    public static final String EMPLOYEE_DETAILS_ALREADY_WITH_MOBILE_EXISTS="Employee details already exists with mobile number";
    public static final String EMPLOYEE_DETAILS_ALREADY_WITH_EMAIL_EXISTS="Employee details already exists with email";
    public static final String EDUCATION_DETAILS_NOT_FOUND="Education details not exists in system!!";
    public static final String PROJECT_DETAILS_NOT_FOUND="Project details not exists in system!!";
    public static final String TECHNO_STACK_NOT_EXISTS="Techno stack not exists in the system!!";

    public static final String setExperience(Date startDate,Date endDate) {
        LocalDate fromDate = startDate.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
        LocalDate toDate = null;
        if (endDate!=null) {
            toDate = endDate.toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();
        } else {
            toDate = LocalDate.now();
        }
        Period diff = Period.between(fromDate, toDate);
        Integer years = diff.getYears();
        Integer months = diff.getMonths();
        if (years!=null && years!=0 && months!=null && months!=0) {
            return years +"." + months + " Years";
        } else if (years!=null && years!=0 && months!=null && months==0){
            return years +" Years";
        } else if (years!=null && years==0 && months!=null && months!=0) {
            return "0."+months+" months";
        } else {
            return Constants.DASH;
        }
    }
}
