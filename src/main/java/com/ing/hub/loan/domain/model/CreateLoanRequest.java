package com.ing.hub.loan.domain.model;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class CreateLoanRequest {
    @NotNull
    private Long customerId;

    @NotNull
    @DecimalMin(value = "0.01")
    private BigDecimal amount; // principal

    @NotNull
    @DecimalMin("0.1")
    @DecimalMax("0.5")
    private BigDecimal interestRate; // e.g., 0.2 for 20%

    @NotNull
    @Pattern(regexp = "6|9|12|24", message = "numberOfInstallments must be one of 6, 9, 12, 24")
    private String numberOfInstallments;
}
