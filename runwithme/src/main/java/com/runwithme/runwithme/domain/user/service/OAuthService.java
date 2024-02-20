package com.runwithme.runwithme.domain.user.service;

import java.util.Date;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.runwithme.runwithme.domain.user.dto.SocialLoginViewDto;
import com.runwithme.runwithme.domain.user.dto.UserInfoResponse;
import com.runwithme.runwithme.domain.user.entity.ProviderType;
import com.runwithme.runwithme.domain.user.entity.User;
import com.runwithme.runwithme.domain.user.external.OAuthHttpRequestHelper;
import com.runwithme.runwithme.domain.user.repository.UserRepository;
import com.runwithme.runwithme.global.security.jwt.AuthToken;
import com.runwithme.runwithme.global.security.jwt.AuthTokenFactory;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class OAuthService {

	private final UserRepository userRepository;
	private final OAuthHttpRequestHelper oAuthHttpRequestHelper;
	private final AuthTokenFactory authTokenFactory;

	public SocialLoginViewDto getByAccessToken(ProviderType providerType, String accessToken) {
		UserInfoResponse userInfoResponse = oAuthHttpRequestHelper.getUserInfo(providerType, accessToken);

		User user = userRepository.findByProviderTypeAndResourceId(providerType, userInfoResponse.id())
			.orElseGet(() -> userRepository.save(User.create(providerType, userInfoResponse.id())));

		Long now = System.currentTimeMillis();

		Long expiryOfAccessToken = authTokenFactory.getExpiryOfAccessToken(now);
		Long expiryOfRefreshToken = authTokenFactory.getExpiryOfRefreshToken(now);

		AuthToken createdAccessToken = authTokenFactory.createAuthToken(
			String.valueOf(user.getSeq()), user.getRoleValue(), new Date(expiryOfAccessToken)
		);
		AuthToken createdRefreshToken = authTokenFactory.createAuthToken(
			String.valueOf(user.getSeq()), new Date(expiryOfRefreshToken)
		);

		return new SocialLoginViewDto(
			user.getSeq(),
			createdAccessToken.getToken(),
			createdRefreshToken.getToken(),
			user.isInitialized()
		);
	}
}
