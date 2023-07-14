package com.runwithme.runwithme.global.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.runwithme.runwithme.domain.user.dto.UserLoginDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
        UsernamePasswordAuthenticationToken authenticationToken = getAuthenticationToken(request);

        setDetails(request, authenticationToken);
        /*Authentication authenticate;
        try {
            authenticate = this.getAuthenticationManager().authenticate(authenticationToken);
        } catch (Exception e) {
            System.out.println("Error:: " + e);
            throw new AuthenticationServiceException("tqldsfaslkdfjaskdlfjalksdjflkd");
        }
        return authenticate;*/
        return this.getAuthenticationManager().authenticate(authenticationToken);
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
}
