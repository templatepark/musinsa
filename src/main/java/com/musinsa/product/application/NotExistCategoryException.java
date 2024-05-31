package com.musinsa.product.application;

public class NotExistCategoryException extends RuntimeException {
    public NotExistCategoryException(String message) {
        super(message);
    }
}
