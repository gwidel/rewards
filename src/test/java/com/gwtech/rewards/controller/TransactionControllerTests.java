package com.gwtech.rewards.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gwtech.rewards.controller.request.TransactionRequest;
import com.gwtech.rewards.model.Customer;
import com.gwtech.rewards.model.Transaction;
import com.gwtech.rewards.repository.CustomerRepository;
import com.gwtech.rewards.repository.TransactionRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@AutoConfigureMockMvc
@SpringBootTest
public class TransactionControllerTests {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private TransactionRepository transactionRepository;
    @MockBean
    private CustomerRepository customerRepository;
    @Autowired
    private ObjectMapper mapper;

    @Test
    public void testTransactionCreation() throws Exception{
        TransactionRequest request = TransactionRequest.builder()
                .title("FV 1/09/2023")
                .purchaseAt(LocalDateTime.of(2023, 9, 12, 7, 23, 07))
                .purchaseValue(BigDecimal.valueOf(101.15))
                .build();

        Customer customer = Customer.builder()
                .name("Customer Name")
                .id(1l)
                .build();
        Mockito.when(customerRepository.findById(1l))
                .thenReturn(Optional.of(customer));

        Mockito.when(transactionRepository.save(any(Transaction.class)))
                .thenReturn(Transaction.builder()
                        .id(1l)
                        .customer(customer)
                        .title(request.getTitle())
                        .purchaseValue(request.getPurchaseValue())
                        .purchaseAt(request.getPurchaseAt())
                        .build());

        mockMvc.perform(MockMvcRequestBuilders.post("/api/customers/1/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request))
                )
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", containsString("/api/customers/1/transactions/1")));
    }

    @Test
    public void testTransactionCreation_missingFields() throws Exception{
        TransactionRequest request = TransactionRequest.builder()
                .purchaseAt(LocalDateTime.of(2023, 9, 12, 7, 23, 07))
                .purchaseValue(BigDecimal.valueOf(101.15))
                .build();

        mockMvc.perform(MockMvcRequestBuilders.post("/api/customers/1/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request))
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").isNotEmpty())
                .andExpect(jsonPath("$.message").value("Validation errors"))
                .andExpect(jsonPath("$.errors").exists())
                .andExpect(jsonPath("$.errors.title").value("must not be blank"))
                .andExpect(jsonPath("$.timestamp").isNotEmpty())
                .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.name()));
    }

    @Test
    public void testTransactionDetails() throws Exception{

        Customer customer = Customer.builder()
                .name("Customer Name")
                .id(1l)
                .build();

        Transaction transaction = Transaction.builder()
                .id(1l)
                .customer(customer)
                .title("FV 1/09/2023")
                .purchaseAt(LocalDateTime.of(2023, 9, 12, 7, 23, 07))
                .purchaseValue(BigDecimal.valueOf(101.15))
                .build();

        Mockito.when(transactionRepository.findById(1l))
                .thenReturn(Optional.of(transaction));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/customers/1/transactions/1")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(transaction.getId()))
                .andExpect(jsonPath("$.purchaseAt").value("2023-09-12 07:23:07"))
                .andExpect(jsonPath("$.purchaseValue").value(transaction.getPurchaseValue()))
                .andExpect(jsonPath("$.title").value(transaction.getTitle()));
    }

    @Test
    public void testTransactionDetails_invalidCustomerId() throws Exception{

        Customer customer = Customer.builder()
                .name("Customer Name")
                .id(1l)
                .build();

        Transaction transaction = Transaction.builder()
                .id(1l)
                .customer(customer)
                .title("FV 1/09/2023")
                .purchaseAt(LocalDateTime.of(2023, 9, 12, 7, 23, 07))
                .purchaseValue(BigDecimal.valueOf(101.15))
                .build();

        Mockito.when(transactionRepository.findById(1l))
                .thenReturn(Optional.of(transaction));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/customers/2/transactions/1")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").isNotEmpty())
                .andExpect(jsonPath("$.message").value("Invalid customer for transaction id=1."))
                .andExpect(jsonPath("$.timestamp").isNotEmpty())
                .andExpect(jsonPath("$.status").value(HttpStatus.NOT_FOUND.name()));
    }

    @Test
    public void testTransactionDetails_invalidId() throws Exception{

        Mockito.when(transactionRepository.findById(1l))
                .thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/customers/1/transactions/1")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").isNotEmpty())
                .andExpect(jsonPath("$.message").value("Transaction with id=1 not found."))
                .andExpect(jsonPath("$.timestamp").isNotEmpty())
                .andExpect(jsonPath("$.status").value(HttpStatus.NOT_FOUND.name()));
    }

    @Test
    public void testTransactionUpdate() throws Exception{

        TransactionRequest request = TransactionRequest.builder()
                .title("FV 1/09/2023")
                .purchaseAt(LocalDateTime.of(2023, 9, 12, 7, 23, 07))
                .purchaseValue(BigDecimal.valueOf(111.11))
                .build();

        Customer customer = Customer.builder()
                .name("Customer Name")
                .id(1l)
                .build();

        Transaction transaction = Transaction.builder()
                .id(1l)
                .customer(customer)
                .title("FV 1/09/2023")
                .purchaseAt(LocalDateTime.of(2023, 9, 12, 7, 23, 07))
                .purchaseValue(BigDecimal.valueOf(101.15))
                .build();

        Mockito.when(transactionRepository.findById(1l))
                .thenReturn(Optional.of(transaction));

        mockMvc.perform(MockMvcRequestBuilders.put("/api/customers/1/transactions/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(transaction.getId()))
                .andExpect(jsonPath("$.purchaseAt").value("2023-09-12 07:23:07"))
                .andExpect(jsonPath("$.purchaseValue").value(request.getPurchaseValue()))
                .andExpect(jsonPath("$.title").value(request.getTitle()));
    }
}
