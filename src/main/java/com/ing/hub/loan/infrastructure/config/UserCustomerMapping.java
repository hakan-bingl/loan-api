package com.ing.hub.loan.infrastructure.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@ConfigurationProperties(prefix = "app.security")
public class UserCustomerMapping {
    /**
     * Map of username -> customerId (for CUSTOMER role users)
     * Example in application.yml:
     * app.security.userCustomerMap.customer1=1
     */
    private Map<String, Long> userCustomerMap = new HashMap<>();

    public Map<String, Long> getUserCustomerMap() {
        return userCustomerMap;
    }

    public void setUserCustomerMap(Map<String, Long> userCustomerMap) {
        this.userCustomerMap = userCustomerMap;
    }

    public Long resolveCustomerId(String username) {
        return userCustomerMap.get(username);
    }
}
