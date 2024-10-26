package com.lockerapp.lockerbackend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable) // Dezactivează CSRF
                .authorizeHttpRequests(authorize -> authorize
                        .anyRequest().authenticated() // Toate cererile necesită autentificare
                )
                .httpBasic(withDefaults()); // Autentificare HTTP Basic

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        // Creează un utilizator in-memory pentru testare
        UserDetails user = User.withUsername("user")
                .password("{noop}password") // Folosim {noop} pentru a nu cripta parola
                .roles("USER")
                .build();

        return new InMemoryUserDetailsManager(user);
    }
}