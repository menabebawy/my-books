package com.mybooks.oauthservice.config;

import com.mybooks.oauthservice.mapper.UserMapper;
import com.mybooks.oauthservice.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@EnableWebSecurity
@AllArgsConstructor
public class DefaultSecurityConfig {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/user/signup/**")
                .permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .formLogin(withDefaults());

        return http.build();
    }
}
