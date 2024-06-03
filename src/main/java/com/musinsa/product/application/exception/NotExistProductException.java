package com.musinsa.product.application.exception;

public class NotExistProductException extends RuntimeException {
    public NotExistProductException(String message) {
        super(message);
    }
}
