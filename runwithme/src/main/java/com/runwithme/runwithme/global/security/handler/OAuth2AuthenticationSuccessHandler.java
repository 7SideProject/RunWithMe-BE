package com.runwithme.runwithme.global.security.handler;

import com.runwithme.runwithme.domain.user.entity.User;
import com.runwithme.runwithme.global.security.jwt.AuthToken;
import com.runwithme.runwithme.global.security.jwt.AuthTokenFactory;
import com.runwithme.runwithme.global.security.model.PrincipalDetails;
import com.runwithme.runwithme.global.utils.CookieUtils;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.Date;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Value("${jwt.access-token.expiry}")
    private int ACCESS_TOKEN_EXPIRY;

    private static final String REDIRECT_URI = "redirect_uri";
    private final AuthTokenFactory authTokenFactory;
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        getRedirectStrategy().sendRedirect(request, response, determineTargetUrl(request, response, authentication));
    }

    protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        Optional<String> redirectUri = CookieUtils.getCookie(request, REDIRECT_URI)
                .map(Cookie::getValue);

        if(redirectUri.isEmpty()) {
            throw new IllegalArgumentException("We need to contain redirect URI");
        }

        String targetUri = redirectUri.orElse(getDefaultTargetUrl());

        return uriBuild(authentication, targetUri);
    }

    private String uriBuild(Authentication authentication, String targetUri) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(targetUri);

        User user = getUser(authentication);

        builder.queryParam("status", user.getRoleStatus());
        builder.queryParam("token", createToken(authentication));

        return builder.build().toString();
    }

    private String createToken(Authentication authentication) {
        User user = getUser(authentication);

        AuthToken accessToken = authTokenFactory.createAuthToken(user.getEmail(), user.getRoleValue(), new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRY));

        return accessToken.getToken();
    }

    private User getUser(Authentication authentication) {
        return ((PrincipalDetails) authentication.getPrincipal()).getUser();
    }
}
