package com.runwithme.runwithme.global.validation.file;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AllowedMimeType {

	IMAGE(new String[]{"image/png", "image/jpeg", "image/gif", "image/bmp", "image/webp"}),
	;

	private final String[] types;
}
