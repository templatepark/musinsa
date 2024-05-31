package com.musinsa.product.application;

public class NotExistProductException extends RuntimeException {
    public NotExistProductException(String message) {
        super(message);
    }
}
