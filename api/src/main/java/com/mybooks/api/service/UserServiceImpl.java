package com.mybooks.api.service;

import com.mybooks.api.dto.SignupRequestDTO;
import com.mybooks.api.exception.UserAlreadyExistException;
import com.mybooks.api.mapper.UserMapper;
import com.mybooks.api.model.User;
import com.mybooks.api.model.UserEntity;
import com.mybooks.api.model.UserRole;
import com.mybooks.api.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }

    public User addUser(SignupRequestDTO signupRequestDto) {
        userRepository.findByEmail(signupRequestDto.getEmail())
                .ifPresent(s -> {
                    throw new UserAlreadyExistException(signupRequestDto.getEmail());
                });

        User user = User.builder()
                .email(signupRequestDto.getEmail())
                .password(passwordEncoder.encode(signupRequestDto.getPassword()))
                .roles(Collections.singleton(UserRole.ROLE_USER))
                .build();

        UserEntity userEntity = userRepository.save(userMapper.transferToUserEntity(user));
        return userMapper.transferToUser(userEntity);
    }
}
