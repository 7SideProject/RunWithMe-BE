package com.runwithme.runwithme.domain.challenge.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Challenge {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(columnDefinition = "INT UNSIGNED")
    private Long seq;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "manager_seq")
//    private User managerSeq;
    @Column(name = "manager_seq")
    private Long managerSeq;

    @Column(name = "img_seq")
    private Long imgSeq;

    @Column(name = "name", length = 30)
    private String name;

    @Column(name = "description", length = 200)
    private String description;

    @Column(name = "goal_days")
    private Long goalDays;

    @Column(name = "goal_type", length = 10)
    private String goalType;

    @Column(name = "goal_amount")
    private Long goalAmount;

    @Column(name = "date_start")
    private LocalDate dateStart;

    @Column(name = "date_end")
    private LocalDate dateEnd;

    @Column(name = "time_start")
    private LocalDateTime timeStart;

    @Column(name = "time_end")
    private LocalDateTime timeEnd;

    @Column(name = "password", length = 10)
    private String password;

    @Column(name = "cost")
    private Long cost;

    @Column(name = "max_member")
    private Long maxMember;

    @Column(name = "check_yn", nullable = false)
    private boolean checkYN;

    @Column(name = "reg_time")
    private LocalDateTime regTime;

    @Builder
    public Challenge(Long managerSeq, Long imgSeq, String name, String description, Long goalDays, String goalType, Long goalAmount, LocalDate dateStart, LocalDate dateEnd, LocalDateTime timeStart, LocalDateTime timeEnd, String password, Long cost, Long maxMember) {
        this.managerSeq = managerSeq;
        this.imgSeq = imgSeq;
        this.name = name;
        this.description = description;
        this.goalDays = goalDays;
        this.goalType = goalType;
        this.goalAmount = goalAmount;
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
        this.password = password;
        this.cost = cost;
        this.maxMember = maxMember;
    }
}
