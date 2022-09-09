package com.mybooks.oauthservice.service;

import com.mybooks.oauthservice.dto.AuthenticationRequestDTO;
import com.mybooks.oauthservice.exception.InvalidLoginException;
import com.mybooks.oauthservice.exception.UserAlreadyExistException;
import com.mybooks.oauthservice.mapper.UserMapper;
import com.mybooks.oauthservice.model.User;
import com.mybooks.oauthservice.model.UserEntity;
import com.mybooks.oauthservice.model.UserRole;
import com.mybooks.oauthservice.repository.UserRepository;
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
        return userRepository
                .findByEmail(username)
                .map(userMapper::transferToUser)
                .orElseThrow(InvalidLoginException::new);
    }

    public User addUser(AuthenticationRequestDTO requestDTO) throws UserAlreadyExistException {
        userRepository.findByEmail(requestDTO.getEmail())
                .ifPresent(s -> {
                    throw new UserAlreadyExistException(requestDTO.getEmail());
                });

        User user = User.builder()
                .email(requestDTO.getEmail())
                .password(passwordEncoder.encode(requestDTO.getPassword()))
                .roles(Collections.singleton(UserRole.ROLE_USER))
                .build();

        UserEntity savedUser = userRepository.save(userMapper.transferToUserEntity(user));
        return userMapper.transferToUser(savedUser);
    }
}
