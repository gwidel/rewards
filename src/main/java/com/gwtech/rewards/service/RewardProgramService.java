package com.gwtech.rewards.service;

import java.time.YearMonth;
import java.util.Map;

import com.gwtech.rewards.model.Customer;

public interface RewardProgramService {
	Map<Customer, Map<YearMonth, Long>> getRewardsReportForAllCustomers();

	Map<Customer, Map<YearMonth, Long>> getRewardsReportForCustomer(Long customerId);
}
