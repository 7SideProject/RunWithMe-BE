package com.runwithme.runwithme.global.security.jwt;

import static com.runwithme.runwithme.global.result.ResultCode.*;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import com.runwithme.runwithme.global.error.CustomException;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Component
public class AuthTokenFactory {
	private static final String AUTHORITIES_KEY = "role";

	private final Key key;
	private final Long periodOfAccessToken;
	private final Long periodOfRefreshToken;

	public AuthTokenFactory(
		@Value("${jwt.secret}") String secret,
		@Value("${jwt.period.access}") Long periodOfAccessToken,
		@Value("${jwt.period.refresh}") Long periodOfRefreshToken
	) {
		this.key = Keys.hmacShaKeyFor(secret.getBytes());
		this.periodOfAccessToken = periodOfAccessToken;
		this.periodOfRefreshToken = periodOfRefreshToken;
	}

	public AuthToken createAuthToken(String id, Date expiry) {
		return new AuthToken(id, expiry, key);
	}

	public AuthToken createAuthToken(String id, String role, Date expiry) {
		return new AuthToken(id, role, expiry, key);
	}

	public AuthToken createAccessToken(String id, String role, Date expiry) {
		return createAuthToken(id, role, expiry);
	}

	public AuthToken createRefreshToken(Date expiry) {
		return createAuthToken(null, expiry);
	}

	public Long getExpiryOfAccessToken(Long now) {
		return now + periodOfAccessToken;
	}

	public Long getExpiryOfRefreshToken(Long now) {
		return now + periodOfRefreshToken;
	}

	public AuthToken convertAuthToken(String token) {
		return new AuthToken(token, key);
	}

	public Authentication getAuthentication(AuthToken authToken) {
		if (authToken.validate()) {
			Claims claims = authToken.getTokenClaims();
			Collection<? extends GrantedAuthority> authorities =
				Arrays.stream(new String[]{claims.get(AUTHORITIES_KEY).toString()})
					.map(SimpleGrantedAuthority::new)
					.collect(Collectors.toList());

			log.debug("claims subject := [{}]", claims.getSubject());
			User principal = new User(claims.getSubject(), "", authorities);

			return new UsernamePasswordAuthenticationToken(principal, authToken, authorities);
		} else {
			throw new CustomException(FAILED_GENERATE_TOKEN);
		}
	}
}
