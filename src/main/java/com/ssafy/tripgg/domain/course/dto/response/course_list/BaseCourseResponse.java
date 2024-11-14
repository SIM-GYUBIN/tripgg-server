package com.ssafy.tripgg.domain.course.dto.response.course_list;

import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Data
@SuperBuilder
public abstract class BaseCourseResponse {
    private Long id;
    private String title;
    private String description;
    private String region;
    private String thumbnailUrl;
    private LocalDateTime createdAt;
}
