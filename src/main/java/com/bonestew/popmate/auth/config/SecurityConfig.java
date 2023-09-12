package com.bonestew.popmate.auth.config;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

import com.bonestew.popmate.auth.jwt.ExceptionHandlerFilter;
import com.bonestew.popmate.auth.jwt.JwtAccessDeniedHandler;
import com.bonestew.popmate.auth.jwt.JwtAuthenticationEntryPoint;
import com.bonestew.popmate.auth.jwt.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
    private final ExceptionHandlerFilter exceptionHandlerFilter;
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(request -> request.requestMatchers(
                "/docs/**",
                "/openapi/**",
                "/api/v1/oauth/**",
                "/api/v1/popup-stores",
                "/api/v1/popup-stores/home",
                "/api/v1/popup-stores/{popupStoreId}",
                "/api/v1/popup-stores/banner",
                "/api/v1/popup-stores/{popupStoreId}/reservations", // TODO:: STAFF, MANAGER 만 접근할 수 있도록 추후 변경
                "/ws-chat/**"
                ).permitAll()
                .anyRequest().authenticated())
            .sessionManagement(manager -> manager.sessionCreationPolicy(STATELESS))
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
            .addFilterBefore(exceptionHandlerFilter, JwtAuthenticationFilter.class)
            .exceptionHandling(handler -> handler.authenticationEntryPoint(jwtAuthenticationEntryPoint))
            .exceptionHandling(handler-> handler.accessDeniedHandler(jwtAccessDeniedHandler));
        return http.build();
    }
}
