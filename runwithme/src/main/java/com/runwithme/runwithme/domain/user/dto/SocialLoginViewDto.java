package com.runwithme.runwithme.domain.user.dto;

public record SocialLoginViewDto(
	Long id,
	String accessToken,
	String refreshToken,
	boolean isInitialized
) {
}
