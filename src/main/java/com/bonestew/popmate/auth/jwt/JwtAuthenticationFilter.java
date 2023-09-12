package com.bonestew.popmate.auth.jwt;

import static com.bonestew.popmate.user.domain.Role.ROLE_USER;

import com.bonestew.popmate.auth.application.JwtService;
import com.bonestew.popmate.auth.domain.PopmateUser;
import com.bonestew.popmate.user.domain.User;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private static final String BEARER = "Bearer ";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws ServletException, IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith(BEARER)) {
            filterChain.doFilter(request, response);
            return;
        }
        String jwtToken = authHeader.substring(7);
        User user = jwtService.getUserInfo(jwtToken);
        if (StringUtils.hasText(jwtToken) && jwtService.validateToken(jwtToken)) {
            List<GrantedAuthority> authority = List.of(new SimpleGrantedAuthority(ROLE_USER.name()));
            PopmateUser popmateUser = new PopmateUser(user.getUserId(), user.getName(), authority);
            Authentication authentication = new UsernamePasswordAuthenticationToken(popmateUser, jwtToken, authority);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(request, response);
    }
}
