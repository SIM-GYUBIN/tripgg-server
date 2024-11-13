package com.ssafy.tripgg.domain.course.dto.response;

import com.ssafy.tripgg.domain.course.entity.Course;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class NotStartCourseResponse {
    private Long id;
    private String title;
    private String description;
    private String thumbnailUrl;
    private LocalDateTime createdAt;

    public static NotStartCourseResponse from(Course course) {
        return NotStartCourseResponse.builder()
                .id(course.getId())
                .title(course.getTitle())
                .description(course.getDescription())
                .thumbnailUrl(course.getThumbnailUrl())
                .createdAt(course.getCreatedAt())
                .build();
    }
}
