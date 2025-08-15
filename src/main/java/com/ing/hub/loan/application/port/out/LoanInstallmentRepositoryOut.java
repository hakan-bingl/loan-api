package com.ing.hub.loan.application.port.out;

import com.ing.hub.loan.domain.model.Loan;
import com.ing.hub.loan.domain.model.LoanInstallment;

import java.time.OffsetDateTime;
import java.util.List;

public interface LoanInstallmentRepositoryOut {
    LoanInstallment save(LoanInstallment loanInstallment) throws Exception;

    void saveAll(List<LoanInstallment> loanInstallments) throws Exception;

    List<LoanInstallment> findByLoanOrderByDueDateAsc(Loan loan)  throws Exception;;

    List<LoanInstallment> findByLoanAndIsPaidFalseOrderByDueDateAsc(Loan loan)  throws Exception;;

    List<LoanInstallment> findByLoanAndDueDateBeforeAndIsPaidFalseOrderByDueDateAsc(Loan loan, OffsetDateTime cutoffExclusive)  throws Exception;;
}
