package com.raj.security.config;

import com.raj.security.filter.JwtAuthenticationFilter;
import com.raj.security.filter.JwtGenerationFilter;
import com.raj.security.service.DAOUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import org.springframework.security.config.Customizer;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.List;

@Configuration
public class SecurityConfig {

    @Bean
    SecurityFilterChain getSecurityFilterChain(HttpSecurity http) throws Exception {

        http.authorizeHttpRequests((requests) -> requests
            .requestMatchers("/public/**").permitAll()
            .anyRequest().authenticated()
        );

        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.cors().configurationSource(request -> {

            CorsConfiguration corsConfig = new CorsConfiguration();

            corsConfig.setAllowedOrigins(List.of("http://127.0.0.1:50505/"));
            corsConfig.setAllowedMethods(List.of("GET", "POST"));
            corsConfig.setAllowedHeaders(List.of("*"));
            corsConfig.setExposedHeaders(List.of("Authorization"));

            return corsConfig;
        });

        http.csrf().disable();

        http.addFilterBefore(new JwtAuthenticationFilter(), BasicAuthenticationFilter.class);
        http.addFilterAfter(new JwtGenerationFilter(), BasicAuthenticationFilter.class);

        http.httpBasic(Customizer.withDefaults());

        return http.build();
    }

    @Bean
    UserDetailsService getUserDetailsService() {
        return new DAOUserDetailsService();
    }

    @Bean
    PasswordEncoder getPasswordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }
}
