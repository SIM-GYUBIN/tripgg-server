package com.ssafy.tripgg.domain.verification.repository;

import com.ssafy.tripgg.domain.verification.entity.PlaceVerification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlaceVerificationRepository extends JpaRepository<PlaceVerification, Long> {
    boolean existsByCourseProgress_IdAndPlace_Id(Long courseProgressId, Long placeId);
}
