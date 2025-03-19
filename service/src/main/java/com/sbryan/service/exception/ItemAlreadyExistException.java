package com.sbryan.service.exception;

public class ItemAlreadyExistException extends Exception {
    private static final String ITEM_ALREADY_EXIST = "Item already exist";

    public ItemAlreadyExistException() {
        super(ITEM_ALREADY_EXIST);
    }
}
