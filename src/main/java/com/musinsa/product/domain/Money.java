package com.musinsa.product.domain;

import java.math.BigDecimal;

import com.musinsa.product.domain.exception.NegativeMoneyException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class Money {

    @Column(name = "price")
    private BigDecimal value;

    protected Money() {}

    public Money(BigDecimal value) {
        if (value.compareTo(BigDecimal.ZERO) < 0) {
            throw new NegativeMoneyException("0 보다 작을수 없습니다.");
        }
        this.value = value;
    }

    public BigDecimal getValue() {
        return value;
    }
}
