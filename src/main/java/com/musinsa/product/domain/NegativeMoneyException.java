package com.musinsa.product.domain;

public class NegativeMoneyException extends RuntimeException {
    public NegativeMoneyException(String message) {
        super(message);
    }
}
