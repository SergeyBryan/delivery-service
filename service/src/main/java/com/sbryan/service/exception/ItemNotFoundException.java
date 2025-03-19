package com.sbryan.service.exception;

public class ItemNotFoundException extends Exception {

    private static final String ITEM_NOT_FOUND = "Item not found";

    public ItemNotFoundException() {
        super(ITEM_NOT_FOUND);
    }
}
