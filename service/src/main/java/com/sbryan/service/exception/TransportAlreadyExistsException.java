package com.sbryan.service.exception;

import java.util.UUID;
import javax.validation.Valid;

public class TransportAlreadyExistsException extends Exception {
    public TransportAlreadyExistsException(@Valid UUID uuid) {
        super("Transport with uuid " + uuid + " already exists");
    }
}
