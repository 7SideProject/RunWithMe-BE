package com.runwithme.runwithme.domain.user.entity;

import java.time.LocalDateTime;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ConnectHistory {
	@Id
	@GeneratedValue
	@Column(name = "ch_seq")
	private Long seq;

	@ManyToOne(cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
	@JoinColumn(name = "user_seq", nullable = false)
	private User user;

	@Column(name = "ch_connect_datetime", nullable = false)
	private LocalDateTime connectDateTime;

	@Builder
	public ConnectHistory(User user, LocalDateTime connectDateTime) {
		this.user = user;
		this.connectDateTime = connectDateTime;
	}
}
