package com.ing.hub.loan.application.port.out;

import com.ing.hub.loan.domain.model.Customer;
import com.ing.hub.loan.domain.model.Loan;

import java.util.List;

public interface LoanRepositoryOut {
    Loan findById(Long id) throws Exception;

    Loan save(Loan loan) throws Exception;

    List<Loan> findByCustomer(Customer customer) throws Exception;

    List<Loan> findByCustomerId(Long customerId) throws Exception;
}
