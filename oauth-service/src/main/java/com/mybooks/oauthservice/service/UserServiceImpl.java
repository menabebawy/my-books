package com.mybooks.clientservice.service;

import com.mybooks.clientservice.dto.AuthenticationRequestDTO;
import com.mybooks.clientservice.exception.InvalidLoginException;
import com.mybooks.clientservice.exception.UserAlreadyExistException;
import com.mybooks.clientservice.mapper.UserMapper;
import com.mybooks.clientservice.model.User;
import com.mybooks.clientservice.model.UserEntity;
import com.mybooks.clientservice.model.UserRole;
import com.mybooks.clientservice.repository.UserRepository;
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

        UserEntity newUser = userMapper.transferToUserEntity(user);

        UserEntity savedUser = userRepository.save(newUser);
        return userMapper.transferToUser(savedUser);
    }
}
