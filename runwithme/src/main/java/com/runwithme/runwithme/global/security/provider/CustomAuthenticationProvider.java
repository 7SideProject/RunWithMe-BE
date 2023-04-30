package com.runwithme.runwithme.global.security.provider;

import com.runwithme.runwithme.domain.user.entity.User;
import com.runwithme.runwithme.domain.user.service.UserService;
import com.runwithme.runwithme.global.security.exception.DeletedUserException;
import com.runwithme.runwithme.global.security.model.PrincipalDetails;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;

@Slf4j
@RequiredArgsConstructor
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final PasswordEncoder encoder;

    @Resource
    private UserService userService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) authentication;

        String email = (String) token.getPrincipal();
        String password = (String) token.getCredentials();

        try {
            User user = userService.findByEmail(email);
            PrincipalDetails principal = new PrincipalDetails(user, null);

            if (!encoder.matches(password, user.getPassword())) {
                throw new BadCredentialsException("잘못된 패스워드입니다.");
            }

            if (user.isDeleted()) throw new DeletedUserException("삭제된 회원입니다.");

            return new UsernamePasswordAuthenticationToken(principal, password, principal.getAuthorities());
        } catch (IllegalArgumentException e) {
            throw new BadCredentialsException("해당 이메일의 유저가 존재하지 않습니다.");
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
