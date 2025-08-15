package com.ing.hub.loan.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "customer")
public class CustomerEntity extends AbstractAuditingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(precision = 19, scale = 2, nullable = false)
    private BigDecimal creditLimit = BigDecimal.ZERO;

    @Column(precision = 19, scale = 2, nullable = false)
    private BigDecimal usedCreditLimit = BigDecimal.ZERO;
}
