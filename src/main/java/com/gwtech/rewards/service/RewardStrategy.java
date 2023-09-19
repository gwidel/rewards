package com.gwtech.rewards.service;

import com.gwtech.rewards.model.Transaction;

public interface RewardStrategy {
	Long calculate(Transaction transaction);
}
