package com.gwtech.rewards.service.mapper;

import com.gwtech.rewards.controller.response.CustomerResponse;
import com.gwtech.rewards.model.Customer;
import org.springframework.stereotype.Component;

@Component
public class CustomerMapper {
    public CustomerResponse map(Customer customer) {
        return CustomerResponse.builder()
                .id(customer.getId())
                .name(customer.getName())
                .build();
    }
}
