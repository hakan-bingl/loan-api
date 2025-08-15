package com.ing.hub.loan.infrastructure.persistence.service;

import com.ing.hub.loan.application.port.out.CustomerRepositoryOut;
import com.ing.hub.loan.domain.exception.NotFoundException;
import com.ing.hub.loan.domain.model.Customer;
import com.ing.hub.loan.infrastructure.mapper.LoanMapper;
import com.ing.hub.loan.infrastructure.persistence.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CustomerDataService implements CustomerRepositoryOut {
    private final CustomerRepository customerRepository;
    private final LoanMapper loanMapper;

    @Override
    public Customer findById(Long id) throws Exception {
        var entity = customerRepository.findById(id);
        if (entity.isEmpty()) {
            throw new NotFoundException("Customer not found: " + id);
        }

        return loanMapper.toCustomer(entity.get());
    }

    @Override
    public Customer save(Customer customer) throws Exception {
        // First mapping to LoanEntity
        var entity = loanMapper.toCustomerEntity(customer);
        // Save
        entity = customerRepository.save(entity);
        // Then mapping to Loan
        return loanMapper.toCustomer(entity);
    }
}
