package com.ing.hub.loan.infrastructure.service;

import com.ing.hub.loan.application.port.in.LoanUseCase;
import com.ing.hub.loan.application.port.out.CustomerRepositoryOut;
import com.ing.hub.loan.application.port.out.LoanInstallmentRepositoryOut;
import com.ing.hub.loan.application.port.out.LoanRepositoryOut;
import com.ing.hub.loan.domain.exception.BusinessException;
import com.ing.hub.loan.domain.model.*;
import com.ing.hub.loan.domain.util.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class LoanService implements LoanUseCase {

    private static final Set<Integer> ALLOWED_INSTALLMENTS = Set.of(6, 9, 12, 24);

    private final CustomerRepositoryOut customerRepository;
    private final LoanRepositoryOut loanRepository;
    private final LoanInstallmentRepositoryOut installmentRepository;

    @Transactional
    @Override
    public CreateLoanResponse createLoan(CreateLoanRequest req) throws Exception {
        int n = Integer.parseInt(req.getNumberOfInstallments());
        if (!ALLOWED_INSTALLMENTS.contains(n)) {
            throw new BusinessException("NumberOfInstallments must be one of 6,9,12,24");
        }
        if (req.getInterestRate().compareTo(new BigDecimal("0.1")) < 0 ||
                req.getInterestRate().compareTo(new BigDecimal("0.5")) > 0) {
            throw new BusinessException("InterestRate must be between 0.1 and 0.5");
        }

        Customer customer = customerRepository.findById(req.getCustomerId());

        BigDecimal total = Utils.scale2(req.getAmount().multiply(BigDecimal.ONE.add(req.getInterestRate())));

        BigDecimal available = customer.getCreditLimit().subtract(customer.getUsedCreditLimit());
        if (available.compareTo(total) < 0) {
            throw new BusinessException("Insufficient credit limit. Available: " + available);
        }

        Loan loan = Loan.builder()
                .customerId(customer.getId())
                .amount(total)
                .numberOfInstallment(n)
                .isPaid(false)
                .build();
        loan = loanRepository.save(loan);

        // Update customer's used credit limit by total
        customer.setUsedCreditLimit(Utils.scale2(customer.getUsedCreditLimit().add(total)));
        customerRepository.save(customer);

        // Create equal installments
        BigDecimal perInstallment = total.divide(BigDecimal.valueOf(n), 2, java.math.RoundingMode.HALF_UP);
        LocalDate firstDueDate = Utils.firstDayOfNextMonth(LocalDate.now());

        List<LoanInstallment> items = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            LocalDate due = YearMonth.from(firstDueDate).plusMonths(i).atDay(1);
            items.add(LoanInstallment.builder()
                    .loanId(loan.getId())
                    .amount(perInstallment)
                    .dueDate(due)
                    .isPaid(false)
                    .build());
        }
        installmentRepository.saveAll(items);

        return CreateLoanResponse.builder()
                .loanId(loan.getId())
                .totalAmount(total)
                .numberOfInstallments(n)
                .createDate(loan.getInsertedAt())
                .build();
    }

    @Override
    public Loan getLoanById(Long id) throws Exception {
        return loanRepository.findById(id);
    }

    @Override
    public List<Loan> listLoans(Long customerId) throws Exception {
        Customer c = customerRepository.findById(customerId);
        return loanRepository.findByCustomer(c);
    }
}
