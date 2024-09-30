package com.projectx.common.utils;

import org.springframework.stereotype.Component;

@Component
public class LoanUtils {
    public static final String LOAN_DETAILS_NOT_FOUND="Loan details not present in the system!!";
    public static final String LOAN_ALREADY_PRESENT="Loan details already present in the system!!";
    public static final String LOAN_AMOUNT_CANNOT_BE_CHANGE="You cannot be change loan amount once EMI started!!";
    public static final String LOAN_EMI_DETAILS_NOT_FOUND="Loan EMI details not present in the system!!";
    public static final String LOAN_EMI_ALREADY_PRESENT="Loan EMI already paid!!";
    public static final String LOAN_ALREADY_CLOSED="Your loan is already closed!!";
    public static final Integer PERSONAL_LOAN_TYPE=1;
    public static final Integer HOME_LOAN_TYPE=2;
    public static final Integer CREDIT_CARD_LOAN_TYPE=3;
    public static final String PERSONAL_LOAN="Personal Loan";
    public static final String HOME_LOAN="Home Loan";
    public static final String CREDIT_CARD="Credit Card";
    public static final String DASH="-";
    public static final String setLoanType(Integer type) {
        if (type.equals(PERSONAL_LOAN_TYPE)){
            return PERSONAL_LOAN;
        } else if (type.equals(HOME_LOAN_TYPE)) {
            return HOME_LOAN;
        } else if (type.equals(CREDIT_CARD_LOAN_TYPE)) {
            return CREDIT_CARD;
        } else {
            return DASH;
        }
    }

}
