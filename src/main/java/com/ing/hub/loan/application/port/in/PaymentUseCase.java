package com.ing.hub.loan.application.port.in;

import com.ing.hub.loan.domain.model.PayLoanResponse;

import java.math.BigDecimal;

public interface PaymentUseCase {
    PayLoanResponse pay(Long loanId, BigDecimal amount) throws Exception;
}
