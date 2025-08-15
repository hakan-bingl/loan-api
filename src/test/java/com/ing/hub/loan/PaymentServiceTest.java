package com.ing.hub.loan;

import com.ing.hub.loan.application.port.in.LoanUseCase;
import com.ing.hub.loan.application.port.in.PaymentUseCase;
import com.ing.hub.loan.application.port.out.CustomerRepositoryOut;
import com.ing.hub.loan.application.port.out.LoanInstallmentRepositoryOut;
import com.ing.hub.loan.domain.model.CreateLoanRequest;
import com.ing.hub.loan.domain.model.CreateLoanResponse;
import com.ing.hub.loan.domain.model.Customer;
import com.ing.hub.loan.domain.model.PayLoanResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class PaymentServiceTest {

    @Autowired
    private LoanUseCase loanService;
    @Autowired
   private PaymentUseCase paymentService;
    @Autowired
    private LoanInstallmentRepositoryOut installmentRepository;
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
    void payTwoInstallments_whenAmountCoversTwo_andWithinCutoff() throws Exception {
        CreateLoanRequest req = new CreateLoanRequest();
        req.setCustomerId(1L);
        req.setAmount(new BigDecimal("1000.00"));
        req.setInterestRate(new BigDecimal("0.2")); // total 1200
        req.setNumberOfInstallments("12");          // per installment 100.00
        CreateLoanResponse created = loanService.createLoan(req);

        PayLoanResponse resp = paymentService.pay(created.getLoanId(), new BigDecimal("300.00"));
        // Depending on discount/penalty, at least 2 should be paid if today is on/after first due date window
        assertThat(resp.getInstallmentsPaid()).isGreaterThanOrEqualTo(2);
        assertThat(resp.getTotalAmountSpent()).isLessThan(new BigDecimal("199.99"));
    }
}
