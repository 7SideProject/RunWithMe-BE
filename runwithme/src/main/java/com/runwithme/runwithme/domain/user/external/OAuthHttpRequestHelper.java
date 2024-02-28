package com.runwithme.runwithme.domain.user.external;

import static com.runwithme.runwithme.domain.user.service.properties.OAuthProperties.*;

import java.time.LocalDateTime;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.runwithme.runwithme.domain.user.dto.UserInfoResponse;
import com.runwithme.runwithme.domain.user.entity.ProviderType;
import com.runwithme.runwithme.domain.user.service.properties.OAuthProperties;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OAuthHttpRequestHelper {

	private final RestTemplate restTemplate;
	private final OAuthProperties oAuthProperties;

	public UserInfoResponse getUserInfo(ProviderType providerType, String accessToken) {
		OAuthProviderProperty providerProperty = oAuthProperties.getProviderProperties(providerType);

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		headers.add("Accept", "application/json");
		headers.add("Authorization", "Bearer " + accessToken);

		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(headers);

		ResponseEntity<UserInfoResponse> response = restTemplate.postForEntity(
			providerProperty.userInfoUri(), request, UserInfoResponse.class
		);

		return response.getBody();
	}
}
