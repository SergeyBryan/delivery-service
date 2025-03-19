package com.sbryan.service.exception;

public class CategoryNotFoundException extends Exception {
    public CategoryNotFoundException(Long id) {
        super("CategoryNotFoundException : Category with %d not found".formatted(id));
    }
}
