package com.ing.hub.loan.infrastructure.rest;

import com.ing.hub.loan.application.port.in.LoanInstallmentUseCase;
import com.ing.hub.loan.application.port.in.LoanUseCase;
import com.ing.hub.loan.application.port.in.PaymentUseCase;
import com.ing.hub.loan.domain.exception.BusinessException;
import com.ing.hub.loan.domain.model.*;
import com.ing.hub.loan.infrastructure.config.UserCustomerMapping;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class LoanController {

    private final LoanUseCase loanService;
    private final LoanInstallmentUseCase loanInstallmentUseCase;
    private final PaymentUseCase paymentService;
    private final UserCustomerMapping mapping;

    @PostMapping("v1/loans")
    @PreAuthorize("hasAnyRole('ADMIN','CUSTOMER')")
    public CreateLoanResponse createLoan(@Valid @RequestBody CreateLoanRequest req, Authentication auth) throws Exception {
        if (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_CUSTOMER"))) {
            Long myCustomerId = mapping.resolveCustomerId(auth.getName());
            if (myCustomerId == null || !myCustomerId.equals(req.getCustomerId())) {
                throw new BusinessException("CUSTOMER users can only create loans for their own customerId");
            }
        }
        return loanService.createLoan(req);
    }

    @GetMapping("v1/loans")
    @PreAuthorize("hasAnyRole('ADMIN','CUSTOMER')")
    public List<Loan> listLoans(@RequestParam Long customerId, Authentication auth) throws Exception {
        if (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_CUSTOMER"))) {
            Long myCustomerId = mapping.resolveCustomerId(auth.getName());
            if (myCustomerId == null || !myCustomerId.equals(customerId)) {
                throw new BusinessException("CUSTOMER users can only list their own loans");
            }
        }
        return loanService.listLoans(customerId);
    }

    @GetMapping("v1/loans/{loanId}/installments")
    @PreAuthorize("hasAnyRole('ADMIN','CUSTOMER')")
    public List<LoanInstallment> listInstallments(@PathVariable Long loanId, Authentication auth) throws Exception {
        if (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_CUSTOMER"))) {
            Long myCustomerId = mapping.resolveCustomerId(auth.getName());
            // Validate ownership
            Loan loan = loanService.getLoanById(loanId);
            if (!Objects.equals(loan.getCustomerId(), myCustomerId)) {
                throw new BusinessException("CUSTOMER users can only access their own loan installments");
            }
        }
        return loanInstallmentUseCase.listByLoan(loanId);
    }

    @PostMapping("v1/loans/{loanId}/pay")
    @PreAuthorize("hasAnyRole('ADMIN','CUSTOMER')")
    public PayLoanResponse pay(@PathVariable Long loanId, @Valid @RequestBody PayLoanRequest req, Authentication auth) throws Exception {
        if (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_CUSTOMER"))) {
            Long myCustomerId = mapping.resolveCustomerId(auth.getName());
            // Validate ownership
            Loan loan = loanService.getLoanById(loanId);
            if (!Objects.equals(loan.getCustomerId(), myCustomerId)) {
                throw new BusinessException("CUSTOMER users can only pay their own loans");
            }
        }
        return paymentService.pay(loanId, req.getAmount());
    }
}
