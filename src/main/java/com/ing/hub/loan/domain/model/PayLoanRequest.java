package com.ing.hub.loan.domain.model;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class PayLoanRequest {
    @NotNull
    @DecimalMin("0.01")
    private BigDecimal amount;
}
