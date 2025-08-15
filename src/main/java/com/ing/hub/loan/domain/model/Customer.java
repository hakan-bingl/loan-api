package com.ing.hub.loan.domain.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Customer implements Serializable {
    private Long id;
    private String firstName;
    private String lastName;
    private BigDecimal creditLimit;
    private BigDecimal usedCreditLimit;
}
