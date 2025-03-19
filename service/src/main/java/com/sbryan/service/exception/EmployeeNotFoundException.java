package com.sbryan.service.exception;

public class EmployeeNotFoundException extends Exception {

    private static final String EMPLOYEE_NOT_FOUND = "Employee not found";

    public EmployeeNotFoundException() {
        super(EMPLOYEE_NOT_FOUND);
    }
}
