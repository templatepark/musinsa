package com.musinsa.product.domain.exception;

public class NegativeMoneyException extends RuntimeException {
    public NegativeMoneyException(String message) {
        super(message);
    }
}
