package com.runwithme.runwithme.global.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.runwithme.runwithme.domain.user.dto.UserLoginDto;
import com.runwithme.runwithme.domain.user.entity.User;
import com.runwithme.runwithme.global.security.exception.DeletedUserException;
import com.runwithme.runwithme.global.security.jwt.AuthToken;
import com.runwithme.runwithme.global.security.jwt.AuthTokenFactory;
import com.runwithme.runwithme.global.security.model.PrincipalDetails;
import com.runwithme.runwithme.global.security.properties.JwtProperties;
import com.runwithme.runwithme.global.utils.CookieUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.Date;

import static com.runwithme.runwithme.global.security.properties.JwtProperties.*;
import static org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames.REFRESH_TOKEN;

@Slf4j
@RequiredArgsConstructor
public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthTokenFactory tokenFactory;
    private final JwtProperties properties;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
        UsernamePasswordAuthenticationToken authenticationToken;

        try {
            authenticationToken = getAuthenticationToken(request);
            setDetails(request, authenticationToken);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        Authentication authentication = this.getAuthenticationManager().authenticate(authenticationToken);

        if (isDeletedUser(authentication)) throw new DeletedUserException("삭제된 회원입니다.");

        return authentication;
    }

    private static UsernamePasswordAuthenticationToken getAuthenticationToken(HttpServletRequest request) {
        try {
            UserLoginDto dto = new ObjectMapper().readValue(request.getInputStream(), UserLoginDto.class);
            log.debug("CustomAuthenticationFilter :: email : {}, password : {}", dto.email(), dto.password());
            return new UsernamePasswordAuthenticationToken(dto.email(), dto.password());
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e.getCause());
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) {
        PrincipalDetails details = (PrincipalDetails) authResult.getPrincipal();
        User user = details.getUser();

        AuthToken accessToken = tokenFactory.createAuthToken(user.getEmail(), user.getRole().toString(), new Date(System.currentTimeMillis() + properties.accessTokenExpiry));
        log.debug("Successful Authentication :: AccessToken : {}", accessToken.getToken());
        response.addHeader("Authorization", "Bearer " + accessToken.getToken());

        AuthToken refreshToken = tokenFactory.createAuthToken(properties.secret, new Date(System.currentTimeMillis() + properties.refreshTokenExpiry));
        log.debug("Successful Authentication :: RefreshToken : {}", refreshToken.getToken());
        CookieUtils.deleteCookie(request, response, REFRESH_TOKEN);
        CookieUtils.addCookie(response, REFRESH_TOKEN, refreshToken.getToken(), properties.refreshTokenExpiry);
    }

    private boolean isDeletedUser(Authentication authentication) {
        PrincipalDetails details = (PrincipalDetails) authentication.getPrincipal();

        User user = details.getUser();

        return user.isDeleted();
    }
}
