package com.ing.hub.loan.application.port.out;

import com.ing.hub.loan.domain.model.Customer;

public interface CustomerRepositoryOut {
    Customer findById(Long id) throws Exception;

    Customer save(Customer customer) throws Exception;
}
