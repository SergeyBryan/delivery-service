package com.sbryan.service.exception;

import java.util.UUID;

public class TransportNotFoundException extends Exception {
    public TransportNotFoundException(UUID id) {
        super("Transport %s not found".formatted(id));
    }
}
