package com.ing.hub.loan.domain.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Getter
@Setter
@Builder
public class CreateLoanResponse {
    private Long loanId;
    private BigDecimal totalAmount;
    private int numberOfInstallments;
    private OffsetDateTime createDate;
}
