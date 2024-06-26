package com.runwithme.runwithme.domain.user.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RefreshToken {

	@Id @GeneratedValue
	@Column(name = "rt_seq")
	private Long seq;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_seq", nullable = false, unique = true)
	private User user;

	@Column(name = "rt_token", nullable = false, unique = true)
	private String token;

	@Column(name = "rt_expired_datetime", nullable = false)
	private LocalDateTime expiredDateTime;

	@Builder
	public RefreshToken(User user, String token, LocalDateTime expiredDateTime) {
		this.user = user;
		this.token = token;
		this.expiredDateTime = expiredDateTime;
	}

	public void update(String token, LocalDateTime expiredDateTime) {
		this.token = token;
		this.expiredDateTime = expiredDateTime;
	}
}
