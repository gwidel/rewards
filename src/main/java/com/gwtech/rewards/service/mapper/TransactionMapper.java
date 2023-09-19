package com.gwtech.rewards.service.mapper;

import com.gwtech.rewards.controller.response.TransactionResponse;
import com.gwtech.rewards.model.Transaction;
import org.springframework.stereotype.Component;

@Component
public class TransactionMapper {

    public TransactionResponse map(Transaction transaction) {
        return TransactionResponse.builder()
                .id(transaction.getId())
                .title(transaction.getTitle())
                .purchaseAt(transaction.getPurchaseAt())
                .purchaseValue(transaction.getPurchaseValue())
                .build();
    }
}
