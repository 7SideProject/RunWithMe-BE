package com.runwithme.runwithme.global.security.handler;

import com.runwithme.runwithme.domain.user.entity.User;
import com.runwithme.runwithme.global.security.jwt.AuthToken;
import com.runwithme.runwithme.global.security.jwt.AuthTokenFactory;
import com.runwithme.runwithme.global.security.model.PrincipalDetails;
import com.runwithme.runwithme.global.utils.CookieUtils;
import com.runwithme.runwithme.global.utils.HeaderUtils;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import java.io.IOException;
import java.util.Date;

@Slf4j
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Value("${jwt.access-token.expiry}")
    private int ACCESS_TOKEN_EXPIRY;

    private static final String REDIRECT_URI = "redirect_uri";
    private final AuthTokenFactory authTokenFactory;
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        PrincipalDetails details = (PrincipalDetails) authentication.getPrincipal();

        User user = details.getUser();

        AuthToken accessToken = authTokenFactory.createAuthToken(user.getEmail(), user.getRole(), new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRY));

        HeaderUtils.setAccessToken(response, accessToken.getToken());

        Cookie redirectUri = CookieUtils.getCookie(request, REDIRECT_URI).orElseThrow(IllegalArgumentException::new);

        response.sendRedirect(redirectUri.getValue());
    }
}
