package com.gwtech.rewards;

import com.gwtech.rewards.model.Customer;
import com.gwtech.rewards.model.Transaction;
import org.apache.commons.lang3.RandomStringUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

public class TestObjectsFactory {

    public static Customer createTestCustomer(Long id, String name, List<Transaction> transactions) {
        return Customer.builder()
                .name(name)
                .id(id)
                .transactions(transactions)
                .build();
    }

    public static Transaction createTestTransaction(LocalDateTime now, Month monthOfTransaction, BigDecimal purchaseValue) {
        return Transaction.builder()
                .title(RandomStringUtils.randomAlphabetic(16))
                .purchaseAt(LocalDateTime.of(now.getYear(), monthOfTransaction, 1, 0, 0))
                .purchaseValue(purchaseValue)
                .build();
    }
}
