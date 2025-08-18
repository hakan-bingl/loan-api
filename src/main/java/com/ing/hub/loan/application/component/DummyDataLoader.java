package com.ing.hub.loan.application.component;

import com.ing.hub.loan.application.port.out.CustomerRepositoryOut;
import com.ing.hub.loan.domain.model.Customer;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class DummyDataLoader implements CommandLineRunner {
    private final CustomerRepositoryOut customerRepositoryOut;

    @Override
    public void run(String... args) throws Exception {
        var customerFirst = Customer.builder()
                .firstName("Customer")
                .lastName("First")
                .build();
        customerRepositoryOut.save(customerFirst);

        var customerSecond = Customer.builder()
                .firstName("Customer")
                .lastName("Second")
                .build();
        customerRepositoryOut.save(customerSecond);
    }
}
