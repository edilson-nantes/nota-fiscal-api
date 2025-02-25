package com.edilson.exception;

public class InvalidProductAlterationException extends RuntimeException{
    public InvalidProductAlterationException(String message) {
        super(message);
    }
}
