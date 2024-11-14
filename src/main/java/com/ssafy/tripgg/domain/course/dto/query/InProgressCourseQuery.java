package com.ssafy.tripgg.domain.course.dto.query;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class InProgressCourseQuery {
    private Long id;
    private String title;
    private String description;
    private Integer regionId;
    private String thumbnailUrl;
    private LocalDateTime createdAt;

    private Long totalPlaceNum;
    private Long verifiedPlaceNum;
    private LocalDateTime startedAt;
}
