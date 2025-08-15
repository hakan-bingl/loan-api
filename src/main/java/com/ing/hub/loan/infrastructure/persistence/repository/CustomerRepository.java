package com.ing.hub.loan.infrastructure.persistence.repository;

import com.ing.hub.loan.infrastructure.persistence.entity.CustomerEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends CrudRepository<CustomerEntity, Long> {
}
