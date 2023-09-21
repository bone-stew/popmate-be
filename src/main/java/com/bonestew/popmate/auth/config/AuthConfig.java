package com.bonestew.popmate.auth.config;

import com.bonestew.popmate.auth.domain.PopmateUser;
import com.bonestew.popmate.user.domain.User;
import com.bonestew.popmate.user.persistence.UserDao;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@Configuration
@RequiredArgsConstructor
public class AuthConfig {

    private final UserDao userDao;

    @Bean
    public UserDetailsService userDetailsService() {
        return username ->
        {
            User user = userDao.findById(Long.valueOf(username)).orElseThrow(() -> new UsernameNotFoundException("User not found"));
            return PopmateUser.from(user);
        };
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userDetailsService());
        return daoAuthenticationProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
