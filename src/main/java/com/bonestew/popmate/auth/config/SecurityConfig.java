package com.bonestew.popmate.auth.config;

import static org.springframework.security.config.Customizer.withDefaults;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

import com.bonestew.popmate.auth.domain.PopmateUser;
import com.bonestew.popmate.auth.jwt.ExceptionHandlerFilter;
import com.bonestew.popmate.auth.jwt.JwtAccessDeniedHandler;
import com.bonestew.popmate.auth.jwt.JwtAuthenticationEntryPoint;
import com.bonestew.popmate.auth.jwt.JwtAuthenticationFilter;
import java.util.Arrays;
import java.util.List;

import com.bonestew.popmate.user.domain.User;
import com.bonestew.popmate.user.persistence.UserDao;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.authentication.configuration.EnableGlobalAuthentication;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
    private final ExceptionHandlerFilter exceptionHandlerFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
            .cors(withDefaults())
            .authorizeHttpRequests(request -> request.requestMatchers(
                    "/docs/**",
                    "/openapi/**",
                    "/api/v1/oauth/**",
                    "/api/v1/popup-stores",
                    "/api/v1/popup-stores/home",
                    "/api/v1/popup-stores/{popupStoreId}",
                    "/api/v1/popup-stores/banner",
                    "/api/v1/popup-stores/{popupStoreId}/reservations", // TODO:: STAFF, MANAGER 만 접근할 수 있도록 추후 변경
                    "/ws-chat/**",
                    "/api/v1/chat/thumbnail/{roomId}"
                ).permitAll()
                .anyRequest().authenticated())
            .sessionManagement(manager -> manager.sessionCreationPolicy(STATELESS))
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
            .addFilterBefore(exceptionHandlerFilter, JwtAuthenticationFilter.class)
            .exceptionHandling(handler -> handler.authenticationEntryPoint(jwtAuthenticationEntryPoint))
            .exceptionHandling(handler -> handler.accessDeniedHandler(jwtAccessDeniedHandler));
        return http.build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000", "https://dashboard.popmate.xyz"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "PATCH"));
        configuration.setAllowedHeaders(List.of("*"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

}
