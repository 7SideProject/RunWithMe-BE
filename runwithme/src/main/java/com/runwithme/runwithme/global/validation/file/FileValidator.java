package com.runwithme.runwithme.global.validation.file;

import java.io.IOException;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.tika.Tika;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FileValidator implements ConstraintValidator<FileValidated, MultipartFile> {

	private FileValidated annotation;

	@Override
	public void initialize(FileValidated constraintAnnotation) {
		this.annotation = constraintAnnotation;
	}

	@Override
	public boolean isValid(MultipartFile value, ConstraintValidatorContext context) {
		context.disableDefaultConstraintViolation();

		if (annotation.nullable() && value.isEmpty())
			return true;

		if (value.isEmpty()) {
			context.buildConstraintViolationWithTemplate("파일이 비어있거나 첨부하지 않았습니다.")
				.addConstraintViolation();
			return false;
		}

		final String fileName = value.getOriginalFilename();

		if (!StringUtils.hasText(fileName)) {
			context.buildConstraintViolationWithTemplate("파일명이 존재해야 합니다.")
				.addConstraintViolation();
			return false;
		}

		final AllowedMimeType[] allowedMimeTypes = annotation.types();
		final String encodedMimeType = getMimeTypeBy(value).toLowerCase();
		final String requestMimeType = value.getContentType().toLowerCase();

		for (AllowedMimeType allowedMimeType : allowedMimeTypes) {
			if (!ArrayUtils.contains(allowedMimeType.getTypes(), requestMimeType)) {
				context.buildConstraintViolationWithTemplate("허용되지 않는 타입입니다.")
					.addConstraintViolation();
				return false;
			}
			if (!ArrayUtils.contains(allowedMimeType.getTypes(), encodedMimeType)) {
				context.buildConstraintViolationWithTemplate("파일이 변조되었습니다.")
					.addConstraintViolation();
				return false;
			}
		}
		return true;
	}

	private String getMimeTypeBy(MultipartFile multipartFile) {
		Tika tika = new Tika();
		try {
			return tika.detect(multipartFile.getInputStream());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
