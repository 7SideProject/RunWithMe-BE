package com.runwithme.runwithme.global.repository;

import com.runwithme.runwithme.global.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {

    Optional<Image> findBySavedName(String name);
}
