package com.gwtech.rewards.service;

import com.gwtech.rewards.controller.request.CustomerRequest;
import com.gwtech.rewards.controller.response.CustomerResponse;

public interface CustomerService {
    CustomerResponse getDetails(long id);

    long create(CustomerRequest request);
}
