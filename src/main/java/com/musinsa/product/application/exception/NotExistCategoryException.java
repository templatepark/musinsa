package com.musinsa.product.application.exception;

public class NotExistCategoryException extends RuntimeException {
    public NotExistCategoryException(String message) {
        super(message);
    }
}
