package com.ing.hub.loan.infrastructure.persistence.service;

import com.ing.hub.loan.application.port.out.LoanRepositoryOut;
import com.ing.hub.loan.domain.exception.NotFoundException;
import com.ing.hub.loan.domain.model.Customer;
import com.ing.hub.loan.domain.model.Loan;
import com.ing.hub.loan.infrastructure.mapper.LoanMapper;
import com.ing.hub.loan.infrastructure.persistence.repository.LoanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class LoanDataService implements LoanRepositoryOut {
    private final LoanRepository loanRepository;
    private final LoanMapper loanMapper;

    @Override
    public Loan findById(Long id) throws Exception {
        var entity = loanRepository.findById(id);
        if (entity.isEmpty()) {
            throw new NotFoundException("Loan not found: " + id);
        }

        return loanMapper.toLoan(entity.get());
    }

    @Override
    public Loan save(Loan loan) throws Exception {
        // First mapping to LoanEntity
        var entity = loanMapper.toLoanEntity(loan);
        // Save
        entity = loanRepository.save(entity);
        // Then mapping to Loan
        return loanMapper.toLoan(entity);
    }

    @Override
    public List<Loan> findByCustomer(Customer customer) throws Exception {
        // First mapping to LoanEntity
        var entity = loanMapper.toCustomerEntity(customer);
        // Fetch the entities
        var entities = loanRepository.findByCustomer(entity);
        // Then mapping to Loans
        return loanMapper.toLoans(entities);
    }

    @Override
    public List<Loan> findByCustomerId(Long customerId) throws Exception {
        // Fetch the entities
        var entities = loanRepository.findByCustomerId(customerId);
        // Then mapping to Loans
        return loanMapper.toLoans(entities);
    }
}
