package com.gwtech.rewards.service.impl;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.temporal.TemporalAdjusters;
import java.util.*;

import com.gwtech.rewards.exceptions.NotFoundException;
import com.gwtech.rewards.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import com.gwtech.rewards.model.Customer;
import com.gwtech.rewards.service.RewardProgramService;
import com.gwtech.rewards.service.RewardStrategy;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class RewardProgramServiceImpl implements RewardProgramService {

	private final RewardStrategy rewardStrategy;
	private final CustomerRepository customerRepository;
	private final int numberOfMonths;
	private final boolean includeCurrentMonth;

	public RewardProgramServiceImpl(final RewardStrategy rewardStrategy,
									final CustomerRepository customerRepository,
									@Value("${number.of.months.in.report}") final int numberOfMonths,
									@Value("${should.include.current.month.in.report}") final boolean includeCurrentMonth) {
		this.customerRepository = customerRepository;
		this.rewardStrategy = rewardStrategy;
		this.numberOfMonths = numberOfMonths;
		this.includeCurrentMonth = includeCurrentMonth;
	}

	public Map<Customer, Map<YearMonth, Long>> getRewardsReportForAllCustomers() {

		Pair<LocalDateTime, LocalDateTime> dateRange = prepareDateRange();

		List<Customer> customers = customerRepository.findAllCustomersWithTransactionsInDateRange(
				dateRange.getFirst(), dateRange.getSecond());

		Map<Customer, Map<YearMonth, Long>> result = new HashMap<>();
		customers.forEach(customer -> processCustomerTransactions(dateRange, result, customer));

		return result;
	}

	public Map<Customer, Map<YearMonth, Long>> getRewardsReportForCustomer(Long customerId) {

		Pair<LocalDateTime, LocalDateTime> dateRange = prepareDateRange();

		Optional<Customer> customerResult = customerRepository.findOneByCustomerWithTransactionsInDateRange(
				customerId, dateRange.getFirst(), dateRange.getSecond());

		if (customerResult.isEmpty()) {
			throw new NotFoundException(String.format("Customer with id=%s not found.", customerId));
		}

		Map<Customer, Map<YearMonth, Long>> result = new HashMap<>();
		customerResult.ifPresent(customer -> processCustomerTransactions(dateRange, result, customer));

		return result;
	}

	private void processCustomerTransactions(Pair<LocalDateTime, LocalDateTime> dateRange,
											 Map<Customer, Map<YearMonth, Long>> result,
											 Customer customer) {
		if (!result.containsKey(customer)) {
			result.put(customer, prepareEmptyMonthlyCounters(dateRange));
		}
		Map<YearMonth, Long> monthlyValues = result.get(customer);

		customer.getTransactions().forEach(transaction -> {
			Long transactionReward = rewardStrategy.calculate(transaction);
			YearMonth monthKey = YearMonth.from(transaction.getPurchaseAt());
			monthlyValues.put(monthKey, monthlyValues.get(monthKey) + transactionReward);
		});
	}
	private Map<YearMonth, Long> prepareEmptyMonthlyCounters(Pair<LocalDateTime, LocalDateTime> dateRange) {

		Map<YearMonth, Long> monthsInRange = new HashMap<>();

		LocalDateTime currentMonth = dateRange.getFirst();
		while (currentMonth.isBefore(dateRange.getSecond()) || currentMonth.isEqual(dateRange.getSecond())) {
			YearMonth yearMonth = YearMonth.from(currentMonth);
			monthsInRange.put(yearMonth, 0L);
			currentMonth = currentMonth.plusMonths(1);
		}
		return monthsInRange;
	}

	private Pair<LocalDateTime, LocalDateTime> prepareDateRange() {

		LocalDateTime now = LocalDateTime.now();
		LocalDateTime startDate;
		LocalDateTime endDate;
		if (includeCurrentMonth) {
			startDate = now.minusMonths(numberOfMonths - 1).withDayOfMonth(1).withHour(0)
					.withMinute(0).withSecond(0).withNano(0);
			endDate = now.with(TemporalAdjusters.lastDayOfMonth()).withHour(23)
					.withMinute(59).withSecond(59).withNano(999999999);

		} else {
			startDate = now.minusMonths(numberOfMonths).withDayOfMonth(1).withHour(0)
					.withMinute(0).withSecond(0).withNano(0);
			endDate = now.withDayOfMonth(1).withHour(0)
					.withMinute(0).withSecond(0).withNano(0).minusSeconds(1);
		}
		return Pair.of(startDate, endDate);
	}
}
