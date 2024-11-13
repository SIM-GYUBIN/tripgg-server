package com.ssafy.tripgg.domain.course.dto.response;

import com.ssafy.tripgg.domain.course.entity.Course;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class AllCourseResponse {
    private Long id;
    private String title;
    private String description;
    private String thumbnailUrl;
    private LocalDateTime createdAt;

    public static AllCourseResponse from(Course course) {
        return AllCourseResponse.builder()
                .id(course.getId())
                .title(course.getTitle())
                .description(course.getDescription())
                .thumbnailUrl(course.getThumbnailUrl())
                .createdAt(course.getCreatedAt())
                .build();
    }
}
