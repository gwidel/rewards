package com.gwtech.rewards.service;

import com.gwtech.rewards.controller.request.TransactionRequest;
import com.gwtech.rewards.controller.response.TransactionResponse;

public interface TransactionService {

    long create(TransactionRequest request, long customerId);

    TransactionResponse getDetails(long id, long customerId);

    TransactionResponse update(long id, long customerId, TransactionRequest request);
}
