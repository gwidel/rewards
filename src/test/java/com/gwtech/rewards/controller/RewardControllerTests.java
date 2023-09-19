package com.gwtech.rewards.controller;

import com.gwtech.rewards.TestObjectsFactory;
import com.gwtech.rewards.model.Customer;
import com.gwtech.rewards.model.Transaction;
import com.gwtech.rewards.repository.CustomerRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static com.gwtech.rewards.TestObjectsFactory.*;


@AutoConfigureMockMvc
@SpringBootTest
public class RewardControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomerRepository customerRepository;

    @Test
    public void testOneCustomerRewards() throws Exception {
        LocalDateTime now = LocalDateTime.now();
        List<Transaction> transactions = List.of(
                createTestTransaction(now, now.getMonth(), BigDecimal.valueOf(120.00)),
                createTestTransaction(now, now.getMonth(), BigDecimal.valueOf(50.00)),
                createTestTransaction(now, now.getMonth().minus(2), BigDecimal.valueOf(50.01)),
                createTestTransaction(now, now.getMonth().minus(1), BigDecimal.valueOf(100.01)));
        Customer customer = createTestCustomer(1L, "Customer A", transactions);

        Mockito.when(
                customerRepository.findOneByCustomerWithTransactionsInDateRange(
                        any(Long.class), any(LocalDateTime.class), any(LocalDateTime.class))).thenReturn(Optional.of(customer));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/rewards?customerId=1")
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$[0].customerName").value("Customer A"))
                        .andExpect(jsonPath("$[0].customerId").value(1))
                        .andExpect(jsonPath("$[0].rewards").isArray())
                        .andExpect(jsonPath("$[0].rewards", hasSize(3)))
                        .andExpect(jsonPath("$[0].rewards[0].yearMonth").value(YearMonth.from(now.minusMonths(2)).toString()))
                        .andExpect(jsonPath("$[0].rewards[0].rewardValue").value(1))
                        .andExpect(jsonPath("$[0].rewards[1].yearMonth").value(YearMonth.from(now.minusMonths(1)).toString()))
                        .andExpect(jsonPath("$[0].rewards[1].rewardValue").value(52))
                        .andExpect(jsonPath("$[0].rewards[2].yearMonth").value(YearMonth.from(now).toString()))
                        .andExpect(jsonPath("$[0].rewards[2].rewardValue").value(90))
                        .andExpect(jsonPath("$[0].totalRewardValue").value(143));
    }


    @Test
    public void testAllCustomersRewards() throws Exception {
        LocalDateTime now = LocalDateTime.now();

        Customer customerA = createTestCustomer(1L, "Customer A",
                List.of(createTestTransaction(now, now.getMonth(), BigDecimal.valueOf(120.00))));

        Customer customerB = createTestCustomer(2L, "Customer B",
                List.of(createTestTransaction(now, now.getMonth(), BigDecimal.valueOf(100.00))));

        Customer customerC = createTestCustomer(3L, "Customer C", new ArrayList<>());

        Mockito.when(
                customerRepository.findAllCustomersWithTransactionsInDateRange(
                        any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(List.of(customerA, customerB, customerC));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/rewards")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].customerName").value("Customer A"))
                .andExpect(jsonPath("$[0].customerId").value(1))
                .andExpect(jsonPath("$[0].totalRewardValue").value(90))
                .andExpect(jsonPath("$[1].customerName").value("Customer B"))
                .andExpect(jsonPath("$[1].customerId").value(2))
                .andExpect(jsonPath("$[1].totalRewardValue").value(50))
                .andExpect(jsonPath("$[2].customerName").value("Customer C"))
                .andExpect(jsonPath("$[2].customerId").value(3))
                .andExpect(jsonPath("$[2].totalRewardValue").value(0));
    }

    @Test
    public void testHandlingNotFoundException() throws Exception {
        Mockito.when(
                customerRepository.findOneByCustomerWithTransactionsInDateRange(
                        any(Long.class), any(LocalDateTime.class), any(LocalDateTime.class))).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/rewards?customerId=1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Customer with id=1 not found."))
                .andExpect(jsonPath("$.timestamp").isNotEmpty())
                .andExpect(jsonPath("$.status").value("NOT_FOUND"));
    }

    @Test
    public void testHandlingBadRequestException() throws Exception {
        Mockito.when(
                customerRepository.findOneByCustomerWithTransactionsInDateRange(
                        any(Long.class), any(LocalDateTime.class), any(LocalDateTime.class))).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/rewards?customerId=abc")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").isNotEmpty())
                .andExpect(jsonPath("$.timestamp").isNotEmpty())
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"));
    }


}
