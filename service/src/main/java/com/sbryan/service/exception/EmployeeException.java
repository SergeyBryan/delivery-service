package com.sbryan.service.exception;


public class EmployeeException extends Exception {

    private static final String EMPLOYEE_UNKNOWN_EXCEPTION = "UNKNOWN EXCEPTION";

    public EmployeeException() {
        super(EMPLOYEE_UNKNOWN_EXCEPTION);
    }
}
