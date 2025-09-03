package com.middleware.parametrizacion.exception;

public class ParametrizacionException extends RuntimeException {
    
    public ParametrizacionException(String message) {
        super(message);
    }
    
    public ParametrizacionException(String message, Throwable cause) {
        super(message, cause);
    }
}
