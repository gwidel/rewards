package com.gwtech.rewards.controller.request;

import java.time.YearMonth;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MonthlyRewardDto {
	private YearMonth yearMonth;
	private Long rewardValue;
}
