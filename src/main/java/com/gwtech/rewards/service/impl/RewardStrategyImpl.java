package com.gwtech.rewards.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.springframework.stereotype.Service;

import com.gwtech.rewards.model.Transaction;
import com.gwtech.rewards.service.RewardStrategy;

@Service
public class RewardStrategyImpl implements RewardStrategy {

    private static final BigDecimal FIRST_REWARD_LIMIT = BigDecimal.valueOf(50);
    private static final BigDecimal SECOND_REWARD_LIMIT = BigDecimal.valueOf(100);
    private static final BigDecimal SECOND_REWARD_MILTIPLIER = BigDecimal.valueOf(2);
    
	@Override
	public Long calculate(Transaction transaction) {
		if (transaction.getPurchaseValue().compareTo(FIRST_REWARD_LIMIT) > 0
			&& transaction.getPurchaseValue().compareTo(SECOND_REWARD_LIMIT) <= 0) {

			return transaction.getPurchaseValue().subtract(FIRST_REWARD_LIMIT).setScale(0, RoundingMode.DOWN).longValue();
			
		} else if (transaction.getPurchaseValue().compareTo(SECOND_REWARD_LIMIT) > 0) {
			
			return transaction.getPurchaseValue().subtract(SECOND_REWARD_LIMIT).setScale(0, RoundingMode.DOWN)
					.multiply(SECOND_REWARD_MILTIPLIER)
					.add(SECOND_REWARD_LIMIT.subtract(FIRST_REWARD_LIMIT)).setScale(0, RoundingMode.DOWN).longValue();
		} else
			return 0L;
	}
}
