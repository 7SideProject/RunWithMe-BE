package com.runwithme.runwithme.global.security.jwt;

import static com.runwithme.runwithme.global.result.ResultCode.*;

import java.security.Key;
import java.util.Date;

import com.runwithme.runwithme.global.error.jwt.JwtException;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class AuthToken {
	@Getter
	private final String token;
	private final Key key;

	private static final String AUTHORITIES_KEY = "role";

	AuthToken(String id, Date expiry, Key key) {
		this.key = key;
		this.token = createAuthToken(id, expiry);
	}

	AuthToken(String id, String role, Date expiry, Key key) {
		this.key = key;
		this.token = createAuthToken(id, role, expiry);
	}

	private String createAuthToken(String id, Date expiry) {
		return Jwts.builder()
			.setSubject(id)
			.signWith(key, SignatureAlgorithm.HS256)
			.setExpiration(expiry)
			.compact();
	}

	private String createAuthToken(String id, String role, Date expiry) {
		return Jwts.builder()
			.setSubject(id)
			.claim(AUTHORITIES_KEY, role)
			.signWith(key, SignatureAlgorithm.HS256)
			.setExpiration(expiry)
			.compact();
	}

	public boolean validate() {
		return this.getTokenClaims() != null;
	}

	public Claims getTokenClaims() {
		try {
			return Jwts.parserBuilder()
				.setSigningKey(key)
				.build()
				.parseClaimsJws(token)
				.getBody();
		} catch (SignatureException e) {
			throw new JwtException(INVALID_JWT_SIGNATURE);
		} catch (MalformedJwtException e) {
			throw new JwtException(INVALID_JWT_TOKEN);
		} catch (ExpiredJwtException e) {
			throw new JwtException(EXPIRED_JWT_TOKEN);
		} catch (UnsupportedJwtException e) {
			throw new JwtException(UNSUPPORTED_JWT_TOKEN);
		}
	}

	public Claims getExpiredTokenClaims() {
		try {
			Jwts.parserBuilder()
				.setSigningKey(key)
				.build()
				.parseClaimsJws(token)
				.getBody();
		} catch (ExpiredJwtException e) {
			log.info("Expired JWT token.");
			return e.getClaims();
		}
		return null;
	}
}
