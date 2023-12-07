package com.runwithme.runwithme.domain.record.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.runwithme.runwithme.domain.record.entity.RecordCoordinate;

public interface RecordCoordinateRepository extends JpaRepository<RecordCoordinate, Long> {
}
