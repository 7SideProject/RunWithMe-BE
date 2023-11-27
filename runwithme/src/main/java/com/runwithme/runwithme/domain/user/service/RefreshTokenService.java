package com.runwithme.runwithme.domain.user.service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.runwithme.runwithme.domain.user.entity.RefreshToken;
import com.runwithme.runwithme.domain.user.entity.User;
import com.runwithme.runwithme.domain.user.repository.RefreshTokenRepository;
import com.runwithme.runwithme.global.error.CustomException;
import com.runwithme.runwithme.global.result.ResultCode;
import com.runwithme.runwithme.global.security.jwt.AuthToken;
import com.runwithme.runwithme.global.security.jwt.AuthTokenFactory;
import com.runwithme.runwithme.global.utils.LocalDateTimeUtils;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class RefreshTokenService {

	private final RefreshTokenRepository refreshTokenRepository;
	private final AuthTokenFactory tokenFactory;

	public void save(String token, User user, LocalDateTime expiredDateTime) {
		Optional<RefreshToken> optionalRefreshToken = refreshTokenRepository.findByUser(user);

		RefreshToken entity;
		if (optionalRefreshToken.isEmpty()) {
			entity = RefreshToken.builder()
				.token(token)
				.user(user)
				.expiredDateTime(expiredDateTime)
				.build();
		} else {
			entity = optionalRefreshToken.get();
			entity.update(token, expiredDateTime);
		}
		refreshTokenRepository.save(entity);
	}

	public AuthToken reIssueAccessToken(String token) {
		RefreshToken entity = findBy(token);

		AuthToken converted = tokenFactory.convertAuthToken(entity.getToken());
		if (converted.validate()) {
			User user = entity.getUser();
			Long expiry = tokenFactory.getExpiryOfAccessToken(System.currentTimeMillis());
			return tokenFactory.createAuthToken(user.getEmail(), user.getRole().toString(), new Date(expiry));
		} else {
			throw new CustomException(ResultCode.UNSUPPORTED_JWT_TOKEN);
		}
	}

	public AuthToken reIssueRefreshToken(String token) {
		RefreshToken entity = findBy(token);

		Long expiryOfNewToken = tokenFactory.getExpiryOfRefreshToken(System.currentTimeMillis());
		AuthToken result = tokenFactory.createAuthToken(null, new Date(expiryOfNewToken));
		save(result.getToken(), entity.getUser(), LocalDateTimeUtils.convertBy(expiryOfNewToken));
		return result;
	}

	public boolean isImminent(String token) {
		RefreshToken entity = findBy(token);
		return isImminent(entity.getExpiredDateTime(), LocalDateTime.now().plusDays(3L));
	}

	private boolean isImminent(LocalDateTime actual, LocalDateTime compared) {
		return actual.isBefore(compared);
	}

	private RefreshToken findBy(String token) {
		return refreshTokenRepository.findByToken(token)
			.orElseThrow(() -> new CustomException(ResultCode.UNSUPPORTED_JWT_TOKEN));
	}
}
