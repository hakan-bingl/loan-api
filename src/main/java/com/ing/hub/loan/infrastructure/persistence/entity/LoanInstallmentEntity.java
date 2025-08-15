package com.ing.hub.loan.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "loan_installment")
public class LoanInstallmentEntity extends AbstractAuditingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "loan_id")
    private LoanEntity loan;

    @Column(precision = 19, scale = 2, nullable = false)
    private BigDecimal amount; // planned equal amount per installment

    @Column(precision = 19, scale = 2)
    private BigDecimal paidAmount = BigDecimal.ZERO; // actual paid amount including discount/penalty

    @Column(columnDefinition = "TIMESTAMP WITH TIME ZONE", nullable = false)
    private LocalDate dueDate;

    @Column(columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private LocalDate paymentDate;

    @Column(nullable = false, columnDefinition = "boolean default false")
    private Boolean isPaid;
}
