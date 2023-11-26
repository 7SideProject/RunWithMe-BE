package com.runwithme.runwithme.domain.user.service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.runwithme.runwithme.domain.user.dto.RefreshTokenDto;
import com.runwithme.runwithme.domain.user.dto.RefreshTokenIssueDto;
import com.runwithme.runwithme.domain.user.entity.RefreshToken;
import com.runwithme.runwithme.domain.user.entity.User;
import com.runwithme.runwithme.domain.user.repository.RefreshTokenRepository;
import com.runwithme.runwithme.domain.user.repository.UserRepository;
import com.runwithme.runwithme.global.error.CustomException;
import com.runwithme.runwithme.global.result.ResultCode;
import com.runwithme.runwithme.global.security.jwt.AuthToken;
import com.runwithme.runwithme.global.security.jwt.AuthTokenFactory;
import com.runwithme.runwithme.global.security.properties.JwtProperties;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class RefreshTokenService {
	private final RefreshTokenRepository refreshTokenRepository;
	private final UserRepository userRepository;
	private final AuthTokenFactory tokenFactory;
	private final JwtProperties properties;

	public void save(String token, User user, LocalDateTime expiredDateTime) {
		Optional<RefreshToken> optionalRefreshToken = refreshTokenRepository.findByUser(user);

		RefreshToken entity;
		if (optionalRefreshToken.isEmpty()) {
			entity = RefreshToken.builder()
				.name(token)
				.user(user)
				.expiredDateTime(expiredDateTime)
				.build();
		} else {
			entity = optionalRefreshToken.get();
			entity.update(token, expiredDateTime);
		}
		refreshTokenRepository.save(entity);
	}

	public void save(RefreshTokenDto dto) {
		User user = userRepository.findByEmail(dto.userEmail())
			.orElseThrow(() -> new CustomException(ResultCode.EMAIL_NOT_FOUND));

		save(dto.tokenName(), user, dto.expiredDatetime());
	}

	public AuthToken reIssueAccessToken(RefreshTokenIssueDto dto) {
		RefreshToken entity = refreshTokenRepository.findByName(dto.refreshToken())
			.orElseThrow(() -> new CustomException(ResultCode.UNSUPPORTED_JWT_TOKEN));

		AuthToken refreshToken = tokenFactory.convertAuthToken(entity.getName());
		if (refreshToken.validate()) {
			User user = entity.getUser();
			Date expiry = new Date(System.currentTimeMillis() + properties.accessTokenExpiry);
			return tokenFactory.createAuthToken(user.getEmail(), user.getRole().toString(), expiry);
		} else {
			throw new CustomException(ResultCode.UNSUPPORTED_JWT_TOKEN);
		}
	}

	public Optional<AuthToken> reIssueRefreshToken(RefreshTokenIssueDto dto, LocalDateTime compared) {
		RefreshToken entity = refreshTokenRepository.findByName(dto.refreshToken())
			.orElseThrow(() -> new CustomException(ResultCode.UNSUPPORTED_JWT_TOKEN));

		LocalDateTime expiry = entity.getExpiredDateTime();

		if (expiry.isAfter(compared))
			return Optional.empty();

		Date date = new Date(System.currentTimeMillis() + properties.refreshTokenExpiry);
		AuthToken token = tokenFactory.createAuthToken(null, date);
		save(token.getToken(), entity.getUser(), convertBy(date));
		return Optional.of(token);
	}

	private static LocalDateTime convertBy(Date date) {
		return date.toInstant()
			.atZone(ZoneId.systemDefault())
			.toLocalDateTime();
	}
}
