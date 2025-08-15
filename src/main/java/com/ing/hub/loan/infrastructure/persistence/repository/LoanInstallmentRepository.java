package com.ing.hub.loan.infrastructure.persistence.repository;

import com.ing.hub.loan.infrastructure.persistence.entity.LoanEntity;
import com.ing.hub.loan.infrastructure.persistence.entity.LoanInstallmentEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.List;

@Repository
public interface LoanInstallmentRepository extends CrudRepository<LoanInstallmentEntity, Long> {
    List<LoanInstallmentEntity> findByLoanOrderByDueDateAsc(LoanEntity loan);

    List<LoanInstallmentEntity> findByLoanAndIsPaidFalseOrderByDueDateAsc(LoanEntity loan);

    List<LoanInstallmentEntity> findByLoanAndDueDateBeforeAndIsPaidFalseOrderByDueDateAsc(LoanEntity loan, OffsetDateTime cutoffExclusive);
}
