package com.gwtech.rewards.controller.request;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RewardDto {
	private String customerName;
	private Long customerId;
	private List<MonthlyRewardDto> rewards;
	private Long totalRewardValue;
}
