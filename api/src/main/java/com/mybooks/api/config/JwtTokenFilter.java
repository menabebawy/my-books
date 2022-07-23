package com.mybooks.api.config;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.mapstruct.ap.internal.util.Strings.isEmpty;

@AllArgsConstructor
@Slf4j
public class JwtTokenFilter extends OncePerRequestFilter {
    private final UserDetailsService userDetailsService;
    private final JwtSecretKey jwtSecretKey;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        final String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String TOKEN_PREFIX = "Bearer";
        if (header == null || isEmpty(header) || !header.startsWith(TOKEN_PREFIX)) {
            filterChain.doFilter(request, response);
            return;
        }

        String extractedEmail;
        try {
            extractedEmail =
                    Jwts.parser()
                            .setSigningKey(jwtSecretKey.getSecretKeyAsByte())
                            .parseClaimsJws(header.replace(TOKEN_PREFIX, "").trim())
                            .getBody()
                            .getSubject();
        } catch (MalformedJwtException ex) {
            log.error("Invalid JWT token", ex);
            throw ex;
        } catch (ExpiredJwtException ex) {
            log.error("Expired JWT token", ex);
            throw ex;
        } catch (UnsupportedJwtException ex) {
            log.error("Unsupported JWT token", ex);
            throw ex;
        } catch (IllegalArgumentException ex) {
            log.error("JWT claims string is empty.", ex);
            throw ex;
        }

        UserDetails user = userDetailsService.loadUserByUsername(extractedEmail);

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);

        filterChain.doFilter(request, response);
    }
}
