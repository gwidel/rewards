package com.gwtech.rewards.service;

import com.gwtech.rewards.model.Transaction;
import com.gwtech.rewards.service.impl.RewardStrategyImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigDecimal;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RewardStrategyTests {

    RewardStrategy rewardStrategy;

    @BeforeEach
    public void setup() {
        this.rewardStrategy = new RewardStrategyImpl();
    }

    @ParameterizedTest
    @MethodSource("provideTransactions")
    public void testRewardStrategy(Transaction transaction, Long expectedPointsRewards) {
        Long rewardPoints = rewardStrategy.calculate(transaction);
        assertEquals(expectedPointsRewards, rewardPoints);
    }

    static Stream<Arguments> provideTransactions() {
        return Stream.of(
                Arguments.of(Transaction.builder().purchaseValue(BigDecimal.valueOf(10.00)).build(), 0L),
                Arguments.of(Transaction.builder().purchaseValue(BigDecimal.valueOf(49.99)).build(), 0L),
                Arguments.of(Transaction.builder().purchaseValue(BigDecimal.valueOf(50.00)).build(), 0L),
                Arguments.of(Transaction.builder().purchaseValue(BigDecimal.valueOf(50.01)).build(), 0L),

                Arguments.of(Transaction.builder().purchaseValue(BigDecimal.valueOf(51.01)).build(), 1L),
                Arguments.of(Transaction.builder().purchaseValue(BigDecimal.valueOf(51.49)).build(), 1L),
                Arguments.of(Transaction.builder().purchaseValue(BigDecimal.valueOf(51.50)).build(), 1L),
                Arguments.of(Transaction.builder().purchaseValue(BigDecimal.valueOf(51.51)).build(), 1L),
                Arguments.of(Transaction.builder().purchaseValue(BigDecimal.valueOf(51.99)).build(), 1L),
                
                Arguments.of(Transaction.builder().purchaseValue(BigDecimal.valueOf(99.99)).build(), 49L),

                Arguments.of(Transaction.builder().purchaseValue(BigDecimal.valueOf(100.00)).build(), 50L),
                Arguments.of(Transaction.builder().purchaseValue(BigDecimal.valueOf(100.01)).build(), 50L),
                Arguments.of(Transaction.builder().purchaseValue(BigDecimal.valueOf(100.10)).build(), 50L),
                Arguments.of(Transaction.builder().purchaseValue(BigDecimal.valueOf(100.50)).build(), 50L),
                Arguments.of(Transaction.builder().purchaseValue(BigDecimal.valueOf(100.99)).build(), 50L),

                Arguments.of(Transaction.builder().purchaseValue(BigDecimal.valueOf(101.00)).build(), 52L),
                Arguments.of(Transaction.builder().purchaseValue(BigDecimal.valueOf(101.15)).build(), 52L),
                Arguments.of(Transaction.builder().purchaseValue(BigDecimal.valueOf(101.50)).build(), 52L),
                Arguments.of(Transaction.builder().purchaseValue(BigDecimal.valueOf(101.99)).build(), 52L),

                Arguments.of(Transaction.builder().purchaseValue(BigDecimal.valueOf(120.00)).build(), 90L),
                Arguments.of(Transaction.builder().purchaseValue(BigDecimal.valueOf(120.50)).build(), 90L)
        );
    }
}
