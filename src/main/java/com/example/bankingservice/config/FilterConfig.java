package com.example.bankingservice.config;

import com.example.bankingservice.filter.AuthorizationFilter;
import com.example.bankingservice.repository.UserRepository;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.crypto.SecretKey;

@Configuration
public class FilterConfig {

    private final UserRepository userRepository;
    private final SecretKey secretKey;

    public FilterConfig(UserRepository userRepository, SecretKey secretKey) {
        this.userRepository = userRepository;
        this.secretKey = secretKey;
    }

    @Bean
    public FilterRegistrationBean<AuthorizationFilter> authorizationFilter() {
        FilterRegistrationBean<AuthorizationFilter> filterBean =
                new FilterRegistrationBean<>();
        filterBean.setFilter(new AuthorizationFilter(userRepository, secretKey));
        filterBean.addUrlPatterns("/api/users");
        return filterBean;
    }
}
