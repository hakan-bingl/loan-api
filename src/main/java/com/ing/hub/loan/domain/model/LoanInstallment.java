package com.ing.hub.loan.domain.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LoanInstallment implements Serializable {
    private Long id;
    private Long loanId;
    private BigDecimal amount; // planned equal amount per installment
    private BigDecimal paidAmount; // actual paid amount including discount/penalty
    private LocalDate dueDate;
    private LocalDate paymentDate;
    private Boolean isPaid;
}
