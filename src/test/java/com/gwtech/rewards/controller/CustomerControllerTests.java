package com.gwtech.rewards.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gwtech.rewards.controller.request.CustomerRequest;
import com.gwtech.rewards.model.Customer;
import com.gwtech.rewards.repository.CustomerRepository;
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

import java.util.Optional;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest
public class CustomerControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomerRepository customerRepository;

    @Autowired
    private ObjectMapper mapper;

    @Test
    public void testCustomerCreation() throws Exception{
        CustomerRequest request = CustomerRequest.builder()
                .name("Customer Name")
                .build();

        Mockito.when(customerRepository.save(any(Customer.class)))
                .thenReturn(Customer.builder()
                        .name(request.getName())
                        .id(1l)
                        .build());

        mockMvc.perform(MockMvcRequestBuilders.post("/api/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request))
                )
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", containsString("/api/customers/1")));
    }

    @Test
    public void testCustomerDetails() throws Exception{

        Mockito.when(customerRepository.findById(1l))
                .thenReturn(Optional.of(Customer.builder()
                        .name("Customer Name")
                        .id(1l)
                        .build()));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/customers/1")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1l))
                .andExpect(jsonPath("$.name").value("Customer Name"));
    }

    @Test
    public void testCustomerDetails_invalidId() throws Exception{

        Mockito.when(customerRepository.findById(1l))
                .thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/customers/1")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").isNotEmpty())
                .andExpect(jsonPath("$.message").value("Customer with id=1 not found."))
                .andExpect(jsonPath("$.timestamp").isNotEmpty())
                .andExpect(jsonPath("$.status").value(HttpStatus.NOT_FOUND.name()));
    }

    @Test
    public void testCustomerCreation_invalidJson() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.post("/api/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}")
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").isNotEmpty())
                .andExpect(jsonPath("$.message").value("Validation errors"))
                .andExpect(jsonPath("$.errors").exists())
                .andExpect(jsonPath("$.errors.name").value("must not be blank"))
                .andExpect(jsonPath("$.timestamp").isNotEmpty())
                .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.name()));
    }
}
