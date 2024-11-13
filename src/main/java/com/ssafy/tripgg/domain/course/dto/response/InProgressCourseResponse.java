package com.ssafy.tripgg.domain.course.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class InProgressCourseResponse {
    private Long id;
    private String title;
    private String description;
    private String thumbnailUrl;
    private LocalDateTime createdAt;

    private Integer earnedPoints;
    private Integer remainingPlaces;
    private LocalDateTime startedAt;
}
