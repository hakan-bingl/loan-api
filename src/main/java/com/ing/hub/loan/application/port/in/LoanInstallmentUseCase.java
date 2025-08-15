package com.ing.hub.loan.application.port.in;

import com.ing.hub.loan.domain.model.LoanInstallment;

import java.util.List;

public interface LoanInstallmentUseCase {
    List<LoanInstallment> listByLoan(Long loanId) throws Exception;
}
