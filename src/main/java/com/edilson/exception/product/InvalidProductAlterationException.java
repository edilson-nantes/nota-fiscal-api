package com.edilson.exception.product;

public class InvalidProductAlterationException extends RuntimeException{
    public InvalidProductAlterationException(String message) {
        super(message);
    }
}
