package com.ing.hub.loan.infrastructure.persistence.service;

import com.ing.hub.loan.application.port.out.LoanInstallmentRepositoryOut;
import com.ing.hub.loan.domain.model.Loan;
import com.ing.hub.loan.domain.model.LoanInstallment;
import com.ing.hub.loan.infrastructure.mapper.LoanMapper;
import com.ing.hub.loan.infrastructure.persistence.repository.LoanInstallmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class LoanInstallmentDataService implements LoanInstallmentRepositoryOut {
    private final LoanInstallmentRepository installmentRepository;
    private final LoanMapper loanMapper;

    @Override
    public LoanInstallment save(LoanInstallment loanInstallment) throws Exception {
        // First mapping to LoanInstallmentEntity
        var entity = loanMapper.toLoanInstallmentEntity(loanInstallment);
        // Save
        entity = installmentRepository.save(entity);
        // Then mapping to LoanInstallment
        return loanMapper.toLoanInstallment(entity);
    }

    @Override
    public void saveAll(List<LoanInstallment> loanInstallments) throws Exception {
        // First mapping to LoanInstallmentEntity
        var entities = loanMapper.toLoanInstallmentEntities(loanInstallments);
        // Save All
        installmentRepository.saveAll(entities);
    }

    @Override
    public List<LoanInstallment> findByLoanOrderByDueDateAsc(Loan loan) throws Exception {
        // First mapping to LoanEntity
        var entity = loanMapper.toLoanEntity(loan);
        // Fetch the entities
        var entities = installmentRepository.findByLoanOrderByDueDateAsc(entity);
        // Then mapping to LoanInstallments
        return loanMapper.toLoanInstallments(entities);
    }

    @Override
    public List<LoanInstallment> findByLoanAndIsPaidFalseOrderByDueDateAsc(Loan loan) throws Exception {
        // First mapping to LoanEntity
        var entity = loanMapper.toLoanEntity(loan);
        // Fetch the entities
        var entities = installmentRepository.findByLoanAndIsPaidFalseOrderByDueDateAsc(entity);
        // Then mapping to LoanInstallments
        return loanMapper.toLoanInstallments(entities);
    }

    @Override
    public List<LoanInstallment> findByLoanAndDueDateBeforeAndIsPaidFalseOrderByDueDateAsc(Loan loan, OffsetDateTime cutoffExclusive) throws Exception {
        // First mapping to LoanEntity
        var entity = loanMapper.toLoanEntity(loan);
        // Fetch the entities
        var entities = installmentRepository.findByLoanAndDueDateBeforeAndIsPaidFalseOrderByDueDateAsc(entity, cutoffExclusive);
        // Then mapping to LoanInstallments
        return loanMapper.toLoanInstallments(entities);
    }
}
