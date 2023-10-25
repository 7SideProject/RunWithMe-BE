package com.runwithme.runwithme.global.security.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.runwithme.runwithme.global.security.jwt.AuthTokenFactory;

@Configuration
public class JwtConfig {
	@Value("${jwt.secret}")
	private String secret;

	@Bean
	public AuthTokenFactory authTokenFactory() {
		return new AuthTokenFactory(secret);
	}
}
