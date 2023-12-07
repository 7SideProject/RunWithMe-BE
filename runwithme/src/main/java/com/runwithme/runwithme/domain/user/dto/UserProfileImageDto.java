package com.runwithme.runwithme.domain.user.dto;

import org.springframework.web.multipart.MultipartFile;

import com.runwithme.runwithme.global.validation.file.AllowedMimeType;
import com.runwithme.runwithme.global.validation.file.FileValidated;

public record UserProfileImageDto(
	@FileValidated(
		types = AllowedMimeType.IMAGE,
		nullable = false)
	MultipartFile image
) {

}
