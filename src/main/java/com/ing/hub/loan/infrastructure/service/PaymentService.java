package com.ing.hub.loan.infrastructure.service;

import com.ing.hub.loan.application.port.in.PaymentUseCase;
import com.ing.hub.loan.application.port.out.CustomerRepositoryOut;
import com.ing.hub.loan.application.port.out.LoanInstallmentRepositoryOut;
import com.ing.hub.loan.application.port.out.LoanRepositoryOut;
import com.ing.hub.loan.domain.exception.BusinessException;
import com.ing.hub.loan.domain.model.Customer;
import com.ing.hub.loan.domain.model.Loan;
import com.ing.hub.loan.domain.model.LoanInstallment;
import com.ing.hub.loan.domain.model.PayLoanResponse;
import com.ing.hub.loan.domain.util.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@RequiredArgsConstructor
@Service
public class PaymentService implements PaymentUseCase {

    private final LoanRepositoryOut loanRepository;
    private final LoanInstallmentRepositoryOut installmentRepository;
    private final CustomerRepositoryOut customerRepository;

    @Transactional
    @Override
    public PayLoanResponse pay(Long loanId, BigDecimal amount) throws Exception {
        if (amount.compareTo(new BigDecimal("0.01")) < 0) {
            throw new BusinessException("Payment amount must be >= 0.01");
        }

        Loan loan = loanRepository.findById(loanId);
        if (loan.getIsPaid()) {
            throw new BusinessException("Loan is already fully paid");
        }

        LocalDate now = LocalDate.now();
        LocalDate cutoffExclusive = Utils.firstDayOfMonth(now.plusMonths(3)); // < cutoff means within current + next 2 months
        List<LoanInstallment> eligible = installmentRepository.findByLoanAndIsPaidFalseOrderByDueDateAsc(loan).stream()
                .filter(i -> i.getDueDate().isBefore(cutoffExclusive))
                .toList();

        BigDecimal remaining = amount.setScale(2, RoundingMode.HALF_UP);
        BigDecimal spent = BigDecimal.ZERO;
        int count = 0;
        BigDecimal limitReduceSum = BigDecimal.ZERO;

        for (LoanInstallment inst : eligible) {
            if (remaining.compareTo(new BigDecimal("0.00")) <= 0)
                break;

            BigDecimal base = inst.getAmount();

            long daysEarly = 0;
            long daysLate = 0;
            if (now.isBefore(inst.getDueDate())) {
                daysEarly = ChronoUnit.DAYS.between(now, inst.getDueDate());
            } else if (now.isAfter(inst.getDueDate())) {
                daysLate = ChronoUnit.DAYS.between(inst.getDueDate(), now);
            }

            BigDecimal adjustment = base.multiply(new BigDecimal("0.001"))
                    .multiply(new BigDecimal(daysEarly > 0 ? daysEarly : daysLate));
            adjustment = adjustment.setScale(2, RoundingMode.HALF_UP);

            BigDecimal paidAmount = base;
            if (daysEarly > 0) {
                paidAmount = paidAmount.subtract(adjustment); // discount
            } else if (daysLate > 0) {
                paidAmount = paidAmount.add(adjustment); // penalty
            }
            paidAmount = paidAmount.setScale(2, RoundingMode.HALF_UP);

            if (remaining.compareTo(paidAmount) >= 0) {
                // Pay this installment fully
                inst.setIsPaid(true);
                inst.setPaidAmount(paidAmount);
                inst.setPaymentDate(now);
                installmentRepository.save(inst);

                remaining = remaining.subtract(paidAmount);
                spent = spent.add(paidAmount);
                count++;
                // decrease customer's usedCreditLimit by the BASE (planned) amount
                limitReduceSum = limitReduceSum.add(base);
            } else {
                // cannot partially pay; stop
                break;
            }
        }

        // Update loan and customer if any payments happened
        if (count > 0) {
            // If no unpaid installments remain, mark loan as paid
            boolean anyUnpaidLeft = installmentRepository.findByLoanAndIsPaidFalseOrderByDueDateAsc(loan).stream().findAny().isPresent();
            if (!anyUnpaidLeft) {
                loan.setIsPaid(true);
                loanRepository.save(loan);
            }

            Customer customer = customerRepository.findById(loan.getCustomerId());
            customer.setUsedCreditLimit(Utils.scale2(customer.getUsedCreditLimit().subtract(limitReduceSum)));
            if (customer.getUsedCreditLimit().compareTo(BigDecimal.ZERO) < 0) {
                customer.setUsedCreditLimit(BigDecimal.ZERO);
            }
            customerRepository.save(customer);
        }

        return PayLoanResponse.builder()
                .installmentsPaid(count)
                .totalAmountSpent(spent.setScale(2, RoundingMode.HALF_UP))
                .loanFullyPaid(loan.getIsPaid())
                .build();
    }
}
