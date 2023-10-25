package com.runwithme.runwithme.domain.record.entity;

import java.time.LocalDate;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;

import com.runwithme.runwithme.global.entity.Image;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
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

    @Column(name = "user_seq", nullable = false)
    private Long userSeq;

    @Column(name = "challenge_seq", nullable = false)
    private Long challengeSeq;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "image_seq")
    private Image image;

    @Column(name = "start_time")
    private String startTime;

    @Column(name = "end_time")
    private String endTime;

    @Column(name = "running_time")
    private Long runningTime;

    @Column(name = "running_distance")
    private Long runningDistance;

    @Column(name = "avgSpeed")
    private Long avgSpeed;

    @Column(name = "calorie")
    private Long calorie;

    // ERD에 lat, lng으로 나눠져 있는데 어떻게 사용하신다는지 몰라 일단 남겨 둠 - TY
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "runRecord")
    private List<RecordCoordinate> coordinates;

    @Column(name = "weekly")
    private int weekly;

    @Column(name = "success_yn")
    private char successYn;

    @Column(name = "reg_time", nullable = false)
    @CreationTimestamp
    private LocalDate regTime;

    @Builder
    public RunRecord(Long userSeq, Long challengeSeq, String startTime, String endTime, Long runningTime, Long runningDistance, Image image, int weekly, char successYn){
        this.userSeq = userSeq;
        this.challengeSeq = challengeSeq;
        this.startTime = startTime;
        this.endTime = endTime;
        this.runningTime = runningTime;
        this.runningDistance = runningDistance;
        this.image = image;
        this.weekly = weekly;
        this.successYn = successYn;
    }
}
