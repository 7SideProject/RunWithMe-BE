package com.runwithme.runwithme.domain.user.controller;

import static org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames.*;

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
import com.runwithme.runwithme.global.utils.CookieUtils;
import com.runwithme.runwithme.global.utils.HeaderUtils;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/token")
@RequiredArgsConstructor
public class RefreshTokenController {
	private final RefreshTokenService refreshTokenService;

	@PostMapping
	public ResponseEntity<ResultResponseDto> reIssue(
		@Validated @ModelAttribute RefreshTokenIssueDto dto,
		BindingResult bindingResult,
		HttpServletResponse response
	) throws NoSuchMethodException, MethodArgumentNotValidException {
		if (bindingResult.hasFieldErrors()) {
			throw new MethodArgumentNotValidException(
				new MethodParameter(
					this.getClass()
						.getDeclaredMethod("reIssue", RefreshTokenIssueDto.class, BindingResult.class, HttpServletResponse.class),
					0),
				bindingResult);
		}

		reIssueAccessToken(dto, response);
		if (refreshTokenService.isImminent(dto.refreshToken()))
			reIssueRefreshToken(dto, response);
		return new ResponseEntity<>(ResultResponseDto.of(ResultCode.USER_REQUEST_SUCCESS), HttpStatus.OK);
	}

	private void reIssueAccessToken(RefreshTokenIssueDto dto, HttpServletResponse response) {
		AuthToken accessToken = refreshTokenService.reIssueAccessToken(dto.refreshToken());
		HeaderUtils.setAccessToken(response, accessToken.getToken());
	}

	private void reIssueRefreshToken(RefreshTokenIssueDto dto, HttpServletResponse response) {
		AuthToken refreshToken = refreshTokenService.reIssueRefreshToken(dto.refreshToken());
		CookieUtils.addCookie(response, REFRESH_TOKEN, refreshToken.getToken());
	}
}
