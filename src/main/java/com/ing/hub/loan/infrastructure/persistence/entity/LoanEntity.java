package com.ing.hub.loan.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "loan")
public class LoanEntity extends AbstractAuditingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private CustomerEntity customer;

    @Column(precision = 19, scale = 2, nullable = false)
    private BigDecimal amount; // total amount (principal + interest)

    @Column(nullable = false)
    private Integer numberOfInstallment;

    @Column(nullable = false, columnDefinition = "boolean default false")
    private Boolean isPaid;
}
