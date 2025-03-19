package com.sbryan.service.exception;

public class ElasticException extends Exception {

    private static final String DEFAULT_MESSAGE = "Elastic Exception";

    public ElasticException(String message) {
        super(DEFAULT_MESSAGE + " : " + message);
    }
}
