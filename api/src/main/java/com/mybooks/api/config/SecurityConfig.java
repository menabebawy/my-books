package com.mybooks.api.config;

import com.mybooks.api.repository.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final UserRepository userRepository;
    private final UserDetailsService userDetailsService;
    private final JwtSecretKey jwtSecretKey;

    public SecurityConfig(UserRepository userRepository, UserDetailsService userDetailsService, JwtSecretKey jwtSecretKey) {
        this.userRepository = userRepository;
        this.userDetailsService = userDetailsService;
        this.jwtSecretKey = jwtSecretKey;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors()
                .and()
                .csrf()
                .disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/login", "/signup", "/refresh-token")
                .permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .addFilterBefore(
                        new JwtTokenFilter(userDetailsService, jwtSecretKey),
                        UsernamePasswordAuthenticationFilter.class
                );

    }

    @Override
    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }
}
