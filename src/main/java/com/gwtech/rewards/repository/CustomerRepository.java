package com.gwtech.rewards.repository;

import com.gwtech.rewards.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    @Query(value = "SELECT DISTINCT c FROM Customer c " +
            "LEFT JOIN FETCH c.transactions t " +
            "WHERE (c.id = :customerId AND (SIZE(c.transactions) = 0 OR t.purchaseAt BETWEEN :startDate AND :endDate))")
    Optional<Customer> findOneByCustomerWithTransactionsInDateRange(@Param("customerId") Long customerId,
                                                                    @Param("startDate") LocalDateTime startDate,
                                                                    @Param("endDate")LocalDateTime endDate);

    @Query(value = "SELECT DISTINCT c FROM Customer c " +
            "LEFT JOIN FETCH c.transactions t " +
            "WHERE (SIZE(c.transactions) = 0 OR t.purchaseAt BETWEEN :startDate AND :endDate)")
    List<Customer> findAllCustomersWithTransactionsInDateRange(@Param("startDate") LocalDateTime startDate,
                                                               @Param("endDate")LocalDateTime endDate);
}
