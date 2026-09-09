package com.CustomerService.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http) throws Exception {

        http
                // Enable CORS
                .cors(cors -> {})

                // Disable CSRF for REST APIs
                .csrf(AbstractHttpConfigurer::disable)

                // Authorization rules
                .authorizeHttpRequests(auth -> auth
                        // Allow browser CORS preflight requests
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
 
                        // Login and registration are public
                        .requestMatchers(
                                "/api/v1/customers/login",
                                "/api/v1/customers/register",
                                "/api/v1/customers/{customerId}",
                                "/api/v1/customers/email/{email}",
                                "/api/v1/customers/mobile/{mobileNumber}",
                                "/api/v1/customers/number/{customerNumber}",
                                "/api/v1/customers/{customerId}",
                                "/api/v1/customers/{customerId}/status",
                                "/api/v1/customers/{customerId}/activate",
                                "/api/v1/customers/{customerId}/block",
                                "/api/v1/customers/{customerId}/close",
                                "/api/v1/customers/{customerId}",
                                "/api/v1/customers/{customerId}/contacts",
                                "/api/v1/customers/{customerId}/addresses",
                                "/api/v1/customers/{customerId}/addresses/primary",
                                "/api/v1/customers/{customerId}/addresses/{addressId}",
                                "/api/v1/customers/{customerId}/addresses/{addressId}/set-primary",
                                "/api/v1/customers/{customerId}/addresses/{addressId}"

                        ).permitAll()

                        // Currently allow all other APIs
                        .anyRequest().permitAll()
                );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}