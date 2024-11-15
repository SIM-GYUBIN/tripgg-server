package com.ssafy.tripgg.domain.verification.entity;

import com.ssafy.tripgg.domain.course.entity.CourseProgress;
import com.ssafy.tripgg.domain.place.entity.Place;
import com.ssafy.tripgg.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "place_verifications")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class PlaceVerification extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_progress_id", nullable = false)
    private CourseProgress courseProgress;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "place_id", nullable = false)
    private Place place;

    @Column(name = "verified_latitude", nullable = false, precision = 10, scale = 8)
    private BigDecimal verifiedLatitude;

    @Column(name = "verified_longitude", nullable = false, precision = 11, scale = 8)
    private BigDecimal verifiedLongitude;

    @Column(name = "photo_verified")
    private Boolean photoVerified;

    @Column(name = "photo_url")
    private String photoUrl;

    @Builder.Default
    @Column(name = "verified_at", nullable = false)
    private LocalDateTime verifiedAt = LocalDateTime.now();

    public void photoVerify(String photoUrl) {
        this.photoVerified = true;
        this.photoUrl = photoUrl;
    }
}
