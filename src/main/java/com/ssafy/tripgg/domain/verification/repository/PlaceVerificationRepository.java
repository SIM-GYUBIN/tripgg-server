package com.ssafy.tripgg.domain.verification.repository;

import com.ssafy.tripgg.domain.verification.entity.PlaceVerification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlaceVerificationRepository extends JpaRepository<PlaceVerification, Long> {
    boolean existsByCourseProgress_IdAndPlace_Id(Long courseProgressId, Long placeId);

    Optional<PlaceVerification> findByCourseProgress_IdAndPlace_Id(Long courseProgressId, Long placeId);

    List<PlaceVerification> findByCourseProgress_Id(Long courseProgressId);
}
