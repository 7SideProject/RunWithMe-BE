package com.runwithme.runwithme.domain.user.controller;

import static org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames.*;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.runwithme.runwithme.domain.user.dto.RefreshTokenIssueDto;
import com.runwithme.runwithme.domain.user.service.RefreshTokenService;
import com.runwithme.runwithme.global.result.ResultCode;
import com.runwithme.runwithme.global.result.ResultResponseDto;
import com.runwithme.runwithme.global.security.jwt.AuthToken;
import com.runwithme.runwithme.global.security.properties.JwtProperties;
import com.runwithme.runwithme.global.utils.CookieUtils;
import com.runwithme.runwithme.global.utils.HeaderUtils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/token")
@RequiredArgsConstructor
public class RefreshTokenController {
	private final RefreshTokenService refreshTokenService;
	private final JwtProperties properties;
	@PostMapping
	public ResponseEntity<ResultResponseDto> reIssue(
		@Validated @ModelAttribute RefreshTokenIssueDto dto,
		BindingResult bindingResult,
		HttpServletRequest request,
		HttpServletResponse response
	) throws NoSuchMethodException, MethodArgumentNotValidException {
		if (bindingResult.hasFieldErrors()) {
			throw new MethodArgumentNotValidException(
				new MethodParameter(
					this.getClass()
						.getDeclaredMethod("reIssue", RefreshTokenIssueDto.class, BindingResult.class, HttpServletRequest.class, HttpServletResponse.class),
					0),
				bindingResult);
		}

		AuthToken accessToken = refreshTokenService.reIssueAccessToken(dto);
		HeaderUtils.setAccessToken(response, accessToken.getToken());

		Optional<AuthToken> optionalRefreshToken = refreshTokenService.reIssueRefreshToken(dto, LocalDateTime.now().plusDays(3L));
		if (optionalRefreshToken.isPresent()) {
			AuthToken refreshToken = optionalRefreshToken.get();

			CookieUtils.deleteCookie(request, response, REFRESH_TOKEN);
			CookieUtils.addCookie(response, REFRESH_TOKEN, refreshToken.getToken(), properties.refreshTokenExpiry);
		}

		return new ResponseEntity<>(ResultResponseDto.of(ResultCode.USER_REQUEST_SUCCESS), HttpStatus.OK);
	}
}
