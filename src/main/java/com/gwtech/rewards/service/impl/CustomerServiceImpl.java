package com.gwtech.rewards.service.impl;

import com.gwtech.rewards.controller.request.CustomerRequest;
import com.gwtech.rewards.controller.response.CustomerResponse;
import com.gwtech.rewards.exceptions.NotFoundException;
import com.gwtech.rewards.model.Customer;
import com.gwtech.rewards.repository.CustomerRepository;
import com.gwtech.rewards.service.CustomerService;
import com.gwtech.rewards.service.mapper.CustomerMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {
    
    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;
    
    @Override
    public CustomerResponse getDetails(long id) {
        Customer customer = customerRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Customer with id=%s not found.", id)));
        return customerMapper.map(customer);
    }

    @Override
    @Transactional
    public long create(CustomerRequest request) {
        Customer customer = Customer.builder()
                .name(request.getName())
                .build();
        return customerRepository.save(customer).getId();
    }
}
