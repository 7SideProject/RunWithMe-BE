package com.runwithme.runwithme.domain.record.entity;

import java.time.LocalDate;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;

import com.runwithme.runwithme.domain.challenge.entity.Challenge;
import com.runwithme.runwithme.domain.user.entity.User;
import com.runwithme.runwithme.global.entity.Image;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RunRecord {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column
    private Long seq;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_seq")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "challenge_seq")
    private Challenge challenge;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "image_seq")
    private Image image;

    @Column(name = "running_day")
    private String runningDay;

    @Column(name = "start_time")
    private String startTime;

    @Column(name = "end_time")
    private String endTime;

    @Column(name = "running_time")
    private Long runningTime;

    @Column(name = "running_distance")
    private Long runningDistance;

    @Column(name = "avgSpeed")
    private double avgSpeed;

    @Column(name = "calorie")
    private Long calorie;

    @Column(name = "weekly")
    private int weekly;

    @Column(name = "success_yn")
    private char successYn;

    @Column(name = "reg_time", nullable = false)
    @CreationTimestamp
    private LocalDate regTime;

    @Builder
    public RunRecord(User user, Challenge challenge, String runningDay, String startTime, String endTime, Long runningTime, Long runningDistance, Long calorie, double avgSpeed, Image image, int weekly, char successYn){
        this.user = user;
        this.challenge = challenge;
        this.runningDay = runningDay;
        this.startTime = startTime;
        this.endTime = endTime;
        this.runningTime = runningTime;
        this.runningDistance = runningDistance;
        this.calorie = calorie;
        this.avgSpeed = avgSpeed;
        this.image = image;
        this.weekly = weekly;
        this.successYn = successYn;
    }
}
