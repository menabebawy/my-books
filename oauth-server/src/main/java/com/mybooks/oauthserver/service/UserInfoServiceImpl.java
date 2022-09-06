package com.mybooks.oauthserver.service;

import com.mybooks.oauthserver.dto.*;
import com.mybooks.oauthserver.exception.InvalidLoginException;
import com.mybooks.oauthserver.exception.InvalidRefreshTokenException;
import com.mybooks.oauthserver.exception.UserAlreadyExistException;
import com.mybooks.oauthserver.mapper.UserInfoMapper;
import com.mybooks.oauthserver.model.User;
import com.mybooks.oauthserver.model.UserRole;
import com.mybooks.oauthserver.repository.UserInfoRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserInfoServiceImpl implements UserInfoService {
    private final UserInfoRepository userInfoRepository;
    private final UserInfoMapper userInfoMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userInfoRepository.findByEmail(username)
                .map(userInfoMapper::transferToUser)
                .orElseThrow(() -> new UsernameNotFoundException(username));
    }

    @Override
    public UserResponseDto addUser(SignupRequestDto requestDTO) throws UserAlreadyExistException {
        userInfoRepository.findByEmail(requestDTO.getEmail())
                .stream()
                .findFirst()
                .ifPresent(s -> {
                    throw new UserAlreadyExistException(requestDTO.getEmail());
                });

        User user = User.builder()
                .email(requestDTO.getEmail())
                .name(requestDTO.getName())
                .password(requestDTO.getPassword())
                .roles(Collections.singleton(UserRole.ROLE_USER))
                .build();

        return Optional.of(user)
                .stream()
                .map(userInfoMapper::transferToUserEntity)
                .map(userInfoRepository::save)
                .map(userInfoMapper::transferToUserResponseDto)
                .findFirst()
                .orElseThrow();
    }

    @Override
    public LoginResponseDto login(AuthenticationRequestDto requestDTO) throws InvalidLoginException {
        User user = userInfoRepository.findByEmail(requestDTO.getEmail())
                .map(userInfoMapper::transferToUser)
                .orElseThrow(() -> new InvalidLoginException());

        return null;
    }

    @Override
    public TokenResponseDto refreshToken(HttpServletRequest httpServletRequest) throws InvalidRefreshTokenException {
        return null;
    }
}
