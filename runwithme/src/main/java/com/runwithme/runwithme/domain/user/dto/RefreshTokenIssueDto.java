package com.runwithme.runwithme.domain.user.dto;

import jakarta.validation.constraints.Pattern;

public record RefreshTokenIssueDto(
	@Pattern(regexp = "refresh_token", message = "반드시 'refresh_token'으로 보내야 합니다.")
    String grantType,
    String refreshToken
) {
}
