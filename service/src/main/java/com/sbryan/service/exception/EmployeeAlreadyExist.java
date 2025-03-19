package com.sbryan.service.exception;


public class EmployeeAlreadyExist extends Exception {

    private static final String EMPLOYEE_ALREADY_EXIST = "Employee already exist";

    public EmployeeAlreadyExist() {
        super(EMPLOYEE_ALREADY_EXIST);
    }
}
