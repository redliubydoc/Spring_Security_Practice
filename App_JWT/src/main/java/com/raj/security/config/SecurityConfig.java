package com.raj.security.config;

import com.raj.security.filter.JwtAuthNFilter;
import com.raj.security.provider.JwtAuthNProvider;
import com.raj.security.provider.DAOAuthNProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final JwtAuthNFilter jwtAuthNFilter;

    private final DAOAuthNProvider usernamePasswordAuthNProvider;

    private final JwtAuthNProvider jwtAuthNProvider;

    public SecurityConfig(JwtAuthNFilter jwtAuthNFilter, DAOAuthNProvider usernamePasswordAuthNProvider, JwtAuthNProvider jwtAuthNProvider) {
        this.jwtAuthNFilter = jwtAuthNFilter;
        this.usernamePasswordAuthNProvider = usernamePasswordAuthNProvider;
        this.jwtAuthNProvider = jwtAuthNProvider;
    }

    @Bean
    public SecurityFilterChain getSecurityFilterChain(HttpSecurity httpSecurity) throws Exception {

        httpSecurity.sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        httpSecurity.csrf().disable();

        httpSecurity.addFilterAt(jwtAuthNFilter, BasicAuthenticationFilter.class);

        httpSecurity.authorizeHttpRequests((requests) -> requests
                .requestMatchers("/public/**").permitAll()
                .requestMatchers("/private/admin/**").hasRole("ADMIN")
                .requestMatchers("private/user/**").hasRole("USER")
                .anyRequest().authenticated()
        );

        return httpSecurity.build();
    }


    @Bean
    public AuthenticationManager getAuthenticationManager(HttpSecurity http) throws Exception {

        AuthenticationManager AuthNManager = http.getSharedObject(AuthenticationManagerBuilder.class)
                .authenticationProvider(usernamePasswordAuthNProvider)
                .authenticationProvider(jwtAuthNProvider)
                .build();

        return AuthNManager;
    }
}
