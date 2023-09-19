package com.gwtech.rewards.service.impl;

import com.gwtech.rewards.controller.request.TransactionRequest;
import com.gwtech.rewards.controller.response.TransactionResponse;
import com.gwtech.rewards.exceptions.NotFoundException;
import com.gwtech.rewards.model.Customer;
import com.gwtech.rewards.model.Transaction;
import com.gwtech.rewards.repository.CustomerRepository;
import com.gwtech.rewards.repository.TransactionRepository;
import com.gwtech.rewards.service.TransactionService;
import com.gwtech.rewards.service.mapper.TransactionMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final CustomerRepository customerRepository;
    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper;

    @Transactional
    @Override
    public long create(TransactionRequest request, long customerId) {

        Customer customer = customerRepository
                .findById(customerId)
                .orElseThrow(() -> new NotFoundException(String.format("Customer with id=%s not found.", customerId)));

        Transaction transaction = Transaction.builder()
                .title(request.getTitle())
                .purchaseAt(request.getPurchaseAt())
                .purchaseValue(request.getPurchaseValue())
                .customer(customer)
                .build();

        return transactionRepository.save(transaction).getId();
    }

    @Override
    public TransactionResponse getDetails(long id, long customerId) {

        Transaction transaction = getTransaction(id, customerId);

        return transactionMapper.map(transaction);
    }

    @Transactional
    @Override
    public TransactionResponse update(long id, long customerId, TransactionRequest request) {
        Transaction transaction = getTransaction(id, customerId);
        transaction.setTitle(request.getTitle());
        transaction.setPurchaseAt(request.getPurchaseAt());
        transaction.setPurchaseValue(request.getPurchaseValue());

        transactionRepository.save(transaction);
        return transactionMapper.map(transaction);
    }

    private Transaction getTransaction(long id, long customerId) {
        Transaction transaction = transactionRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Transaction with id=%s not found.", id)));

        if (customerId != transaction.getCustomer().getId()) {
            throw new NotFoundException(String.format("Invalid customer for transaction id=%s.", id));
        }
        return transaction;
    }
}
