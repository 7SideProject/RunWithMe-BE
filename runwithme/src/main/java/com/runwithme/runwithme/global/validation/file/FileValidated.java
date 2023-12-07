package com.runwithme.runwithme.global.validation.file;

import static java.lang.annotation.ElementType.*;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = FileValidator.class)
public @interface FileValidated {

	/**
	 * 기본 메시지를 설정해도 무시됩니다.
	 * @see FileValidator
	 */
	String message() default "";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

	/**
	 * 허용할 타입을 설정합니다.
	 * @see AllowedMimeType
	 */
	AllowedMimeType[] types();

	/**
	 * 파일을 반드시 첨부해야 할 경우, false로 설정합니다.
	 */
	boolean nullable() default true;
}
