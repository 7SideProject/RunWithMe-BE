package com.runwithme.runwithme.domain.challenge.dto;

import org.springframework.web.multipart.MultipartFile;

import com.runwithme.runwithme.global.validation.file.AllowedMimeType;
import com.runwithme.runwithme.global.validation.file.FileValidated;

public record ChallengeImageDto(
	@FileValidated(
		types = AllowedMimeType.IMAGE,
		nullable = false)
	MultipartFile image
) {
}
