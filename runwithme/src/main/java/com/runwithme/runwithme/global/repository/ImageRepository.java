package com.runwithme.runwithme.global.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.runwithme.runwithme.global.entity.Image;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {

	Optional<Image> findBySavedName(String name);
}
