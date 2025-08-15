package com.ing.hub.loan.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/actuator/health", "/h2-console/**").permitAll()
                .anyRequest().authenticated()
            )
            .httpBasic(Customizer.withDefaults());

        // H2 console frames
        http.headers(headers -> headers.frameOptions(frame -> frame.sameOrigin()));
        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        // Passwords use {noop} for demo simplicity
        var admin = User.withUsername("admin")
                .password("{noop}Admin!?")
                .roles("ADMIN")
                .build();

        var customer1 = User.withUsername("customer.first")
                .password("{noop}Customer1!?")
                .roles("CUSTOMER")
                .build();

        var customer2 = User.withUsername("customer.second")
                .password("{noop}Customer2!?")
                .roles("CUSTOMER")
                .build();

        return new InMemoryUserDetailsManager(admin, customer1, customer2);
    }
}
