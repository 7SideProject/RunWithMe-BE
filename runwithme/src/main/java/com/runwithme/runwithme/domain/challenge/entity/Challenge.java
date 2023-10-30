package com.runwithme.runwithme.domain.challenge.entity;

import java.time.LocalDate;
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
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Challenge {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column
	private Long seq;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "manager_seq")
	private User manager;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "image_seq")
	private Image image;

	@Column(name = "name", length = 30, nullable = false)
	private String name;

	@Column(name = "description", length = 200)
	private String description;

	@Min(value = 1, message = "최소값은 1 입니다.")
	@Max(value = 7, message = "최대값은 7 입니다.")
	@Column(name = "goal_days")
	private Long goalDays;

	@Column(name = "goal_type", length = 10)
	private GoalType goalType;

	@Column(name = "goal_amount")
	private Long goalAmount;

	@Column(name = "date_start", nullable = false)
	private LocalDate dateStart;

	@Column(name = "date_end", nullable = false)
	private LocalDate dateEnd;

	@Column(name = "password", length = 10)
	private String password;

	@Min(value = 500, message = "최소값은 500 입니다.")
	@Max(value = 10000, message = "최대값은 10000 입니다.")
	@Column(name = "cost", nullable = false)
	private Long cost;

	@Column(name = "now_member", nullable = false)
	private Long nowMember;

	@Min(value = 2, message = "최소값은 2 입니다.")
	@Max(value = 20, message = "최대값은 20 입니다.")
	@Column(name = "max_member", nullable = false)
	private Long maxMember;

	@Column(name = "delete_yn")
	private char deleteYn;

	@Column(name = "reg_time")
	private LocalDateTime regTime;

	@Builder
	public Challenge(User manager, Image image, String name, String description, Long goalDays, GoalType goalType, Long goalAmount, LocalDate dateStart, LocalDate dateEnd, String password, Long cost, Long nowMember, Long maxMember, char deleteYn) {
		this.manager = manager;
		this.image = image;
		this.name = name;
		this.description = description;
		this.goalDays = goalDays;
		this.goalType = goalType;
		this.goalAmount = goalAmount;
		this.dateStart = dateStart;
		this.dateEnd = dateEnd;
		this.password = password;
		this.cost = cost;
		this.nowMember = nowMember;
		this.maxMember = maxMember;
		this.deleteYn = deleteYn;
	}
	public void plusNowMember() { this.nowMember += 1; }
	public void minusNowMember() { this.nowMember -= 1; }
	public void deleteChallenge() {
		this.deleteYn = 'Y';
	}
}
