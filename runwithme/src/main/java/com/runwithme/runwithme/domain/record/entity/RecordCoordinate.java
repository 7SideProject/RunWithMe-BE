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
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "run_record_seq")
    private RunRecord runRecord;

    @Column
    private int latitude;
    @Column
    private int longitude;
    @Builder
    public RecordCoordinate(RunRecord runRecord, int latitude, int longitude) {
        this.runRecord = runRecord;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
