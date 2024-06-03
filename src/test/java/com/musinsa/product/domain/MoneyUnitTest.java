package com.musinsa.product.domain;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;

import com.musinsa.product.domain.exception.NegativeMoneyException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MoneyUnitTest {

    @DisplayName("Money 생성 성공, value가 0 이상일 경우")
    @Test
    void createMoney_WhenValueIsNonNegative_ShouldSucceed() {
        // given
        BigDecimal value = new BigDecimal(0L);

        // when
        Money money = new Money(value);

        // then
        assertThat(money).isNotNull();
        assertThat(money.getValue()).isEqualTo(value);
    }

    @DisplayName("Money 생성 실패, value가 음수일 경우")
    @Test
    void createMoney_WhenValueIsNegative_ShouldThrowException() {
        // given
        BigDecimal value = new BigDecimal(-1L);

        // when, then
        assertThatThrownBy(() -> new Money(value)).isInstanceOf(NegativeMoneyException.class);
    }
}
