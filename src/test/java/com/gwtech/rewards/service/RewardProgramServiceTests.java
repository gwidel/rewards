package com.gwtech.rewards.service;

import com.gwtech.rewards.exceptions.NotFoundException;
import com.gwtech.rewards.model.Customer;
import com.gwtech.rewards.repository.CustomerRepository;
import com.gwtech.rewards.service.impl.RewardProgramServiceImpl;
import com.gwtech.rewards.service.impl.RewardStrategyImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.gwtech.rewards.TestObjectsFactory.createTestCustomer;
import static com.gwtech.rewards.TestObjectsFactory.createTestTransaction;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
public class RewardProgramServiceTests {

    RewardProgramServiceImpl rewardProgramService;

    CustomerRepository customerRepository = Mockito.mock(CustomerRepository.class);

    @BeforeEach
    public void setup() {
        int numberOfMonths = 3;
        boolean includeCurrentMonth = true;
        this.rewardProgramService = new RewardProgramServiceImpl(
                new RewardStrategyImpl(), customerRepository, numberOfMonths, includeCurrentMonth
        );
    }

    @Test
    public void testRewardsForSingleCustomer_NoCustomersFound() {
        Mockito.when(customerRepository.findOneByCustomerWithTransactionsInDateRange(
                any(Long.class), any(LocalDateTime.class), any(LocalDateTime.class))).thenReturn(Optional.empty());

        Exception exception = assertThrows(NotFoundException.class, () ->
                rewardProgramService.getRewardsReportForCustomer(1L));

        assertTrue(exception.getMessage().contains("Customer with id=1 not found."));
    }

    @Test
    public void testRewardsForSingleCustomer_validateDateRange() {
        LocalDateTime now = LocalDateTime.now();
        Customer customerA = createTestCustomer(1L, "Customer A",
                List.of(createTestTransaction(now, now.getMonth().minus(2), BigDecimal.valueOf(120.00))));
        Mockito.when(
                customerRepository.findOneByCustomerWithTransactionsInDateRange(
                        any(Long.class), any(LocalDateTime.class), any(LocalDateTime.class))).thenReturn(Optional.of(customerA));

        Map<Customer, Map<YearMonth, Long>> calculations = rewardProgramService.getRewardsReportForCustomer(1L);
        assertEquals(3, calculations.get(customerA).entrySet().size());

        assertTrue(calculations.get(customerA).containsKey(YearMonth.from(now.minusMonths(2))));
        assertTrue(calculations.get(customerA).containsKey(YearMonth.from(now.minusMonths(1))));
        assertTrue(calculations.get(customerA).containsKey(YearMonth.from(now)));
    }
}
