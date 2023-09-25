package com.gwtech.rewards.model;

import java.util.List;
import jakarta.persistence.*;

import lombok.*;

@Entity
@Data
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
public class Customer {
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;
    
    @Column
    @EqualsAndHashCode.Include
    private String name;
    
    @OneToMany(mappedBy = "customer")
    private List<Transaction> transactions;
}
