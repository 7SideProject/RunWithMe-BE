package com.runwithme.runwithme.domain.challenge.entity;

import java.time.LocalDateTime;

import com.runwithme.runwithme.domain.user.entity.User;
import com.runwithme.runwithme.global.entity.Image;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChallengeBoard {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column
	private Long seq;

	@Column(name = "challenge_seq")
	private Long challengeSeq;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_seq")
	private User user;

	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
	@JoinColumn(name = "image_seq")
	private Image image;

	@Column(name = "challenge_board_content")
	private String challengeBoardContent;

	@Column(name = "challenge_board_reg_time")
	private LocalDateTime challengeBoardRegTime;

	@Builder
	public ChallengeBoard(User user, Long challengeSeq, Image image, String challengeBoardContent, LocalDateTime challengeBoardRegTime) {
		this.user = user;
		this.challengeSeq = challengeSeq;
		this.image = image;
		this.challengeBoardContent = challengeBoardContent;
		this.challengeBoardRegTime = challengeBoardRegTime;
	}
}
