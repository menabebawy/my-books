package com.mybooks.api.service;

import com.mybooks.api.config.JwtEndpointAccessTokenGenerator;
import com.mybooks.api.config.JwtSecretKey;
import com.mybooks.api.dto.LoginRequestDto;
import com.mybooks.api.dto.SignupRequestDTO;
import com.mybooks.api.dto.TokenResponseDTO;
import com.mybooks.api.exception.InvalidLoginException;
import com.mybooks.api.exception.InvalidRefreshTokenException;
import com.mybooks.api.exception.UserAlreadyExistException;
import com.mybooks.api.mapper.UserMapper;
import com.mybooks.api.model.User;
import com.mybooks.api.model.UserEntity;
import com.mybooks.api.model.UserRole;
import com.mybooks.api.repository.UserRepository;
import io.jsonwebtoken.Jwts;
import lombok.AllArgsConstructor;
import org.apache.http.HttpHeaders;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;

import static org.mapstruct.ap.internal.util.Strings.isEmpty;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final JwtEndpointAccessTokenGenerator jwtEndpointAccessTokenGenerator;
    private final JwtSecretKey jwtSecretKey;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository
                .findByEmail(username)
                .map(userMapper::transferToUser)
                .orElseThrow(InvalidLoginException::new);
    }

    public User addUser(SignupRequestDTO signupRequestDto) throws UserAlreadyExistException {
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

    public TokenResponseDTO login(LoginRequestDto loginRequestDto) throws InvalidLoginException {
        User user = userRepository.findByEmail(loginRequestDto.getEmail())
                .map(userMapper::transferToUser)
                .orElseThrow(InvalidLoginException::new);

        if (!passwordEncoder.matches(loginRequestDto.getPassword(), user.getPassword())) {
            throw new InvalidLoginException();
        }

        return TokenResponseDTO.builder()
                .accessToken(jwtEndpointAccessTokenGenerator.createAccessToken(loginRequestDto.getEmail(), user.getRolesAsString()))
                .refreshToken(jwtEndpointAccessTokenGenerator.createRefreshToken(loginRequestDto.getEmail(), user.getRolesAsString()))
                .build();
    }

    public TokenResponseDTO refreshToken(HttpServletRequest request) throws InvalidRefreshTokenException {
        final String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String TOKEN_PREFIX = "Bearer";
        if (isEmpty(header) || !header.startsWith(TOKEN_PREFIX)) {
            throw new InvalidRefreshTokenException();
        }

        String refreshToken = header.replace(TOKEN_PREFIX, "").trim();

        String extractedEmail;
        try {
            extractedEmail =
                    Jwts.parser()
                            .setSigningKey(jwtSecretKey.getSecretKeyAsByte())
                            .parseClaimsJws(header.replace(TOKEN_PREFIX, "").trim())
                            .getBody()
                            .getSubject();
        } catch (RuntimeException ex) {
            throw new InvalidRefreshTokenException();
        }

        User user = userRepository.findByEmail(loadUserByUsername(extractedEmail).getUsername())
                .map(userMapper::transferToUser)
                .orElseThrow(InvalidRefreshTokenException::new);

        return TokenResponseDTO.builder()
                .accessToken(jwtEndpointAccessTokenGenerator.createAccessToken(user.getEmail(), user.getRolesAsString()))
                .refreshToken(refreshToken)
                .build();
    }
}
