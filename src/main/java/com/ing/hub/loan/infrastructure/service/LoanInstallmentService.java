package com.ing.hub.loan.infrastructure.service;

import com.ing.hub.loan.application.port.in.LoanInstallmentUseCase;
import com.ing.hub.loan.application.port.out.LoanInstallmentRepositoryOut;
import com.ing.hub.loan.application.port.out.LoanRepositoryOut;
import com.ing.hub.loan.domain.model.Loan;
import com.ing.hub.loan.domain.model.LoanInstallment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class LoanInstallmentService implements LoanInstallmentUseCase {

    private final LoanRepositoryOut loanRepository;
    private final LoanInstallmentRepositoryOut loanInstallmentRepository;

    @Override
    public List<LoanInstallment> listByLoan(Long loanId) throws Exception {
        Loan loan = loanRepository.findById(loanId);
        return loanInstallmentRepository.findByLoanOrderByDueDateAsc(loan);
    }
}
