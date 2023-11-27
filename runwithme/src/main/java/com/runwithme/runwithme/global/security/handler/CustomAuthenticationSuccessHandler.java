package com.runwithme.runwithme.global.security.handler;

import static org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.runwithme.runwithme.domain.user.dto.converter.UserConverter;
import com.runwithme.runwithme.domain.user.entity.User;
import com.runwithme.runwithme.domain.user.service.RefreshTokenService;
import com.runwithme.runwithme.global.result.ResultCode;
import com.runwithme.runwithme.global.result.ResultResponseDto;
import com.runwithme.runwithme.global.security.jwt.AuthToken;
import com.runwithme.runwithme.global.security.jwt.AuthTokenFactory;
import com.runwithme.runwithme.global.security.model.PrincipalDetails;
import com.runwithme.runwithme.global.utils.CookieUtils;
import com.runwithme.runwithme.global.utils.HeaderUtils;
import com.runwithme.runwithme.global.utils.LocalDateTimeUtils;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
	private final AuthTokenFactory tokenFactory;
	private final RefreshTokenService refreshTokenService;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
		PrincipalDetails details = (PrincipalDetails) authentication.getPrincipal();
		User user = details.getUser();

		setAccessToken(response, user);
		setRefreshToken(response, user);
	}

	private void setRefreshToken(HttpServletResponse response, User user) {
		Long expiry = tokenFactory.getExpiryOfRefreshToken(System.currentTimeMillis());

		AuthToken refreshToken = tokenFactory.createAuthToken(null, new Date(expiry));
		refreshTokenService.save(refreshToken.getToken(), user, LocalDateTimeUtils.convertBy(expiry));

		log.debug("Successful Authentication :: RefreshToken : {}", refreshToken.getToken());

		CookieUtils.addCookie(response, REFRESH_TOKEN, refreshToken.getToken());
	}

	private void setAccessToken(HttpServletResponse response, User user) throws IOException {
		Long expiry = tokenFactory.getExpiryOfAccessToken(System.currentTimeMillis());
		AuthToken accessToken = tokenFactory.createAuthToken(
			user.getEmail(), user.getRole().toString(), new Date(expiry)
		);

		log.debug("Successful Authentication :: AccessToken : {}", accessToken.getToken());

		HeaderUtils.setAccessToken(response, accessToken.getToken());
		setResponseBody(user, response);
	}

	private void setResponseBody(User loginUser, HttpServletResponse response) throws IOException {
		ObjectMapper om = new ObjectMapper();
		om.registerModule(new JavaTimeModule());
		om.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

		String responseBody = om.writerWithDefaultPrettyPrinter().writeValueAsString(ResultResponseDto.of(ResultCode.LOGIN_SUCCESS, UserConverter.toViewDto(loginUser)));
		PrintWriter writer = response.getWriter();
		writer.write(responseBody);
	}
}
