package com.ing.hub.loan.domain.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Getter
@Setter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Loan implements Serializable {
    private Long id;
    private Long customerId;
    private BigDecimal amount; // total amount (principal + interest)
    private Integer numberOfInstallment;
    private Boolean isPaid;
    private OffsetDateTime insertedAt;
}
