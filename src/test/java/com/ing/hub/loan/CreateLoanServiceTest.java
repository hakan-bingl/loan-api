package com.ing.hub.loan;

import com.ing.hub.loan.application.port.in.LoanUseCase;
import com.ing.hub.loan.application.port.out.CustomerRepositoryOut;
import com.ing.hub.loan.domain.exception.BusinessException;
import com.ing.hub.loan.domain.model.CreateLoanRequest;
import com.ing.hub.loan.domain.model.CreateLoanResponse;
import com.ing.hub.loan.domain.model.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class CreateLoanServiceTest {

    @Autowired
    private LoanUseCase loanService;
    @Autowired
    private CustomerRepositoryOut customerRepository;

    @BeforeEach
    void init() throws Exception {
        // id=1
        customerRepository.save(Customer.builder()
                .firstName("Alice")
                .lastName("Smith")
                .creditLimit(new BigDecimal("10000.00"))
                .usedCreditLimit(new BigDecimal("0.00"))
                .build());

        // id=2
        customerRepository.save(Customer.builder()
                .firstName("Bob")
                .lastName("Smith")
                .creditLimit(new BigDecimal("8000.00"))
                .usedCreditLimit(new BigDecimal("0.00"))
                .build());
    }

    @Test
    void createLoan_success() throws Exception {
        CreateLoanRequest req = new CreateLoanRequest();
        req.setCustomerId(1L);
        req.setAmount(new BigDecimal("1000.00"));
        req.setInterestRate(new BigDecimal("0.3"));
        req.setNumberOfInstallments("6");
        CreateLoanResponse res = loanService.createLoan(req);
        assertThat(res.getLoanId()).isNotNull();
        assertThat(res.getTotalAmount().toPlainString()).isEqualTo("1300.00");
    }

    @Test
    void createLoan_failsOnLimit() {
        CreateLoanRequest req = new CreateLoanRequest();
        req.setCustomerId(2L);
        req.setAmount(new BigDecimal("100000.00"));
        req.setInterestRate(new BigDecimal("0.2"));
        req.setNumberOfInstallments("6");
        assertThrows(BusinessException.class, () -> loanService.createLoan(req));
    }
}
