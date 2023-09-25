package com.gwtech.rewards.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import jakarta.persistence.*;

import lombok.*;

@Entity
@Data
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;
    
    @Column(name = "title")
    @EqualsAndHashCode.Include
    private String title;
    
    @Column(name = "purchase_at")
    @EqualsAndHashCode.Include
    private LocalDateTime purchaseAt;
    
    @Column(name = "purchase_value")
    @EqualsAndHashCode.Include
    private BigDecimal purchaseValue;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;
}
