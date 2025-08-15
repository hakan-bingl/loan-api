package com.ing.hub.loan.infrastructure.persistence.repository;

import com.ing.hub.loan.infrastructure.persistence.entity.CustomerEntity;
import com.ing.hub.loan.infrastructure.persistence.entity.LoanEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoanRepository extends CrudRepository<LoanEntity, Long> {
    List<LoanEntity> findByCustomer(CustomerEntity customer);

    @Query(
            value = "SELECT * FROM orders WHERE customer_id = :customerId",
            nativeQuery = true
    )
    List<LoanEntity> findByCustomerId(@Param("customerId") Long customerId);
}
