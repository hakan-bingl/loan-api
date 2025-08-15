package com.ing.hub.loan.domain.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
public class PayLoanResponse {
    private int installmentsPaid;
    private BigDecimal totalAmountSpent;
    private boolean loanFullyPaid;
}
