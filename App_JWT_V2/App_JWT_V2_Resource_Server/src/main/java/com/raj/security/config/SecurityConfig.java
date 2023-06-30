package com.raj.security.config;

import com.raj.security.filter.AccessTokenAuthNFilter;
import com.raj.security.provider.AccessTokenAuthNProvider;
import com.raj.security.service.AccessTokenService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.List;

@Configuration
public class SecurityConfig {

    private final AccessTokenAuthNFilter accessTokenAuthNFilter;
    private final AccessTokenAuthNProvider accessTokenAuthNProvider;

    public SecurityConfig(AccessTokenAuthNFilter accessTokenAuthNFilter, AccessTokenAuthNProvider accessTokenAuthNProvider) {
        this.accessTokenAuthNFilter = accessTokenAuthNFilter;
        this.accessTokenAuthNProvider = accessTokenAuthNProvider;
    }

    @Bean
    public SecurityFilterChain getSecurityFilterChain(HttpSecurity httpSecurity) throws Exception {

        httpSecurity
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        httpSecurity
            .addFilterAt(accessTokenAuthNFilter, UsernamePasswordAuthenticationFilter.class);

        httpSecurity
            .csrf().disable();

        httpSecurity.cors().configurationSource((request) -> {
            CorsConfiguration corsConfig = new CorsConfiguration();

            corsConfig.setAllowedOrigins(List.of("http://127.0.0.1:50505/", "http://192.168.0.236:50505/" ));
            corsConfig.setAllowedMethods(List.of("GET"));
            corsConfig.setAllowedHeaders(List.of("*"));
            corsConfig.setExposedHeaders(List.of("Authorization"));

            return corsConfig;
        });

        httpSecurity
            .authorizeHttpRequests((request) -> {
                request
                        .requestMatchers("/admin/**").hasAuthority("ADMIN")
                        .requestMatchers("/user/**").hasAuthority("USER")
                        .anyRequest().authenticated();
            });

        return httpSecurity.build();
    }

    @Bean
    public AuthenticationManager getAuthenticationManager(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity.getSharedObject(AuthenticationManagerBuilder.class)
            .authenticationProvider(accessTokenAuthNProvider)
            .build();
    }
}
