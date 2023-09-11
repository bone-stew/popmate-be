package com.bonestew.popmate.auth.config;

import static com.bonestew.popmate.exception.enums.ResultCode.UNAUTHORIZED;

import com.bonestew.popmate.dto.ApiResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    ObjectMapper objectMapper = new ObjectMapper();
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
        throws IOException, ServletException {
        log.info("인증 정보가 유효하지 않습니다.");
        response.setContentType("application/json;charset=UTF-8");
        String result = objectMapper.writeValueAsString(
            new ApiResponse<>(UNAUTHORIZED, "인증 정보가 유효하지 않습니다."));
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.getWriter().write(result);
    }
}
