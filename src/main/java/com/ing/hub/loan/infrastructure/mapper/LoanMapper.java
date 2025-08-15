package com.ing.hub.loan.infrastructure.mapper;

import com.ing.hub.loan.domain.model.Customer;
import com.ing.hub.loan.domain.model.Loan;
import com.ing.hub.loan.domain.model.LoanInstallment;
import com.ing.hub.loan.infrastructure.persistence.entity.CustomerEntity;
import com.ing.hub.loan.infrastructure.persistence.entity.LoanEntity;
import com.ing.hub.loan.infrastructure.persistence.entity.LoanInstallmentEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface LoanMapper {
    LoanMapper INSTANCE = Mappers.getMapper(LoanMapper.class);

    @Mapping(target = "customer.id", source = "customerId")
    LoanEntity toLoanEntity(Loan loan);

    @Mapping(target = "customerId", source = "customer.id")
    Loan toLoan(LoanEntity loanEntity);

    Customer toCustomer(CustomerEntity customerEntity);

    CustomerEntity toCustomerEntity(Customer customer);

    @Mapping(target = "loan.id", source = "loanId")
    LoanInstallmentEntity toLoanInstallmentEntity(LoanInstallment loanInstallment);

    @Mapping(target = "loanId", source = "loan.id")
    LoanInstallment toLoanInstallment(LoanInstallmentEntity loanInstallmentEntity);

    @Mapping(target = "loan.id", source = "loanId")
    List<LoanInstallmentEntity> toLoanInstallmentEntities(List<LoanInstallment> loanInstallments);

    @Mapping(target = "customerId", source = "customer.id")
    List<Loan> toLoans(Iterable<LoanEntity> loans);

    @Mapping(target = "loan", source = "loan")
    List<LoanInstallment> toLoanInstallments(Iterable<LoanInstallmentEntity> loansInstallments);
}
