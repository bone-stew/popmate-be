package com.bonestew.popmate.auth.config;

import static com.bonestew.popmate.exception.enums.ResultCode.TOKEN_EXPIRATION;
import com.bonestew.popmate.dto.ApiResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class ExceptionHandlerFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (ExpiredJwtException ex) {
            // 예외가 발생했을 때 JSON 형식으로 응답을 반환
            ApiResponse<Void> apiResponse = new ApiResponse<>(TOKEN_EXPIRATION, TOKEN_EXPIRATION.getMessage());
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write(new ObjectMapper().writeValueAsString(apiResponse));
        }
    }
}
