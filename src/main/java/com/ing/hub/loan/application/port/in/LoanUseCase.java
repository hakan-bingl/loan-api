package com.ing.hub.loan.application.port.in;

import com.ing.hub.loan.domain.model.CreateLoanRequest;
import com.ing.hub.loan.domain.model.CreateLoanResponse;
import com.ing.hub.loan.domain.model.Loan;

import java.util.List;

public interface LoanUseCase {
    CreateLoanResponse createLoan(CreateLoanRequest req) throws Exception;

    Loan getLoanById(Long id) throws Exception;

    List<Loan> listLoans(Long customerId) throws Exception;
}
