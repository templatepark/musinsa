package com.musinsa.product.application;

public class NotExistBrandException extends RuntimeException {
    public NotExistBrandException(String message) {
        super(message);
    }
}
