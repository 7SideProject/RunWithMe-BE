package com.runwithme.runwithme.domain.challenge.entity;

import com.runwithme.runwithme.domain.user.entity.User;
import lombok.*;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

//    @Column(name = "join_user")
//    private List<Long> users = new ArrayList<>();
}
