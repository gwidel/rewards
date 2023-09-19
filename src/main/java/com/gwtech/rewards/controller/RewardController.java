package com.gwtech.rewards.controller;

import java.time.YearMonth;
import java.util.List;
import java.util.Map;

import com.gwtech.rewards.controller.response.ApiError;
import com.gwtech.rewards.controller.response.CustomerResponse;
import com.gwtech.rewards.model.Customer;
import com.gwtech.rewards.service.mapper.RewardMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gwtech.rewards.controller.request.RewardDto;
import com.gwtech.rewards.service.RewardProgramService;

import lombok.RequiredArgsConstructor;

import static com.gwtech.rewards.controller.SwaggerExamples.EXAMPLE_CUSTOMER_RESPONSE;
import static com.gwtech.rewards.controller.SwaggerExamples.EXAMPLE_REWARDS_REPORT;

@RestController
@RequiredArgsConstructor
@Slf4j
public class RewardController {

	private final RewardProgramService rewardProgramService;
	private final RewardMapper rewardMapper;

	@Operation(summary = "Get Customer rewards report either for single customer or all of them",
			parameters = {
					@Parameter(name = "customerId", in = ParameterIn.QUERY, required = false, description = "Customer identification.")
			},
			responses = {
					@ApiResponse(
							responseCode = "200",
							description = "Successful operation.",
							content = @Content(examples =
							@ExampleObject(name = "Example of Customer Response",
									value = EXAMPLE_REWARDS_REPORT,
									description = "Report Response object"),
									schema = @Schema(implementation = List.class))
					),
					@ApiResponse(
							responseCode = "404",
							description = "Missing specified customer.",
							content = @Content(
									mediaType = "application/json",
									schema = @Schema(implementation = ApiError.class)
							)
					)
			}
	)
	@GetMapping(value = "/api/rewards")
	public ResponseEntity<List<RewardDto>> calculateRewards(@RequestParam(required = false) Long customerId) {
		log.debug("/api/rewards customerId={}", customerId);
		Map<Customer, Map<YearMonth, Long>> calculations = (customerId != null)
				? rewardProgramService.getRewardsReportForCustomer(customerId)
				: rewardProgramService.getRewardsReportForAllCustomers();
		return ResponseEntity.ok(rewardMapper.map(calculations));
	}
}
