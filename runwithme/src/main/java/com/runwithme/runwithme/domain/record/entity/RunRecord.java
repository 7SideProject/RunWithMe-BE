package com.runwithme.runwithme.domain.record.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RunRecord {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(columnDefinition = "INT UNSIGNED")
    private Long seq;

    @Column(name = "user_seq", nullable = false)
    private Long userSeq;

    @Column(name = "challenge_seq", nullable = false)
    private Long challengeSeq;

    @Column(name = "start_time")
    private String startTime;

    @Column(name = "end_time")
    private String endTime;

    @Column(name = "running_time")
    private Long runningTime;

    @Column(name = "running_distance")
    private Long runningDistance;

    @Column(name = "img_seq")
    private Long imgSeq;

    @Column(name = "calorie")
    private Long calorie;

    @Column(name = "avgSpeed")
    private Long avgSpeed;

//    @Column(name = "coordinates")
//    private List<Long> coordinates;

    @Column(name = "reg_time", nullable = false)
    @CreationTimestamp
    private LocalDate regTime;

    @Builder
    public RunRecord(Long userSeq, Long challengeSeq, String startTime, String endTime, Long runningTime, Long runningDistance, Long imgSeq){
        this.userSeq = userSeq;
        this.challengeSeq = challengeSeq;
        this.startTime = startTime;
        this.endTime = endTime;
        this.runningTime = runningTime;
        this.runningDistance = runningDistance;
//        this.coordinates = coordinates;
        this.imgSeq = imgSeq;
    }
}
