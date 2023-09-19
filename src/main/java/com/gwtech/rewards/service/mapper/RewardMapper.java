package com.gwtech.rewards.service.mapper;

import java.time.YearMonth;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.gwtech.rewards.controller.request.MonthlyRewardDto;
import com.gwtech.rewards.controller.request.RewardDto;
import com.gwtech.rewards.model.Customer;

@Component
public class RewardMapper {

	public List<RewardDto> map(Map<Customer, Map<YearMonth, Long>> rewardResults) {
		return rewardResults.entrySet().stream()
				.sorted(Map.Entry.comparingByKey(Comparator.comparing(Customer::getId)))
				.map(entry -> mapCustomerRewards(entry.getKey(), entry.getValue()))
				.collect(Collectors.toList());
	}
	
	public RewardDto mapCustomerRewards(Customer customer, Map<YearMonth, Long> customerRewardResults) {
		
		List<MonthlyRewardDto> rewards = prepareRewardsDto(customerRewardResults);
		Long totalRewards = rewards.stream().map(dto -> dto.getRewardValue()).reduce(0l, Long::sum);
		
		return RewardDto.builder()
				.customerName(customer.getName())
				.customerId(customer.getId())
				.rewards(rewards)
				.totalRewardValue(totalRewards)
				.build();
	}
	
	private List<MonthlyRewardDto> prepareRewardsDto(Map<YearMonth, Long> monthlyValues) {
		
		return monthlyValues.entrySet().stream().map(entry -> 
			MonthlyRewardDto.builder()
					.yearMonth(entry.getKey())
					.rewardValue(entry.getValue())
					.build()
		).collect(Collectors.toList());
	}
}
