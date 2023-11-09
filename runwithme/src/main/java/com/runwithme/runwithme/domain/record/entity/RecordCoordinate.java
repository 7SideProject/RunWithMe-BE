package com.runwithme.runwithme.domain.record.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Setter;

@Setter
@Entity
@Table(name = "t_record_coordinate")
public class RecordCoordinate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long seq;

    @JoinColumn(name = "run_record_seq")
    @ManyToOne(fetch = FetchType.LAZY)
    private RunRecord runRecord;

    @Column
    private double latitude;

    @Column
    private double longitude;

    @Builder
    public RecordCoordinate(RunRecord runRecord, double latitude, double longitude) {
        this.runRecord = runRecord;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
