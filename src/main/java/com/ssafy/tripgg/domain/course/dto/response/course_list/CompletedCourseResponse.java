package com.ssafy.tripgg.domain.course.dto.response.course_list;

import com.ssafy.tripgg.domain.course.entity.Course;
import com.ssafy.tripgg.domain.course.entity.enums.Region;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.util.Objects;

@Getter
@SuperBuilder
public class CompletedCourseResponse extends BaseCourseResponse {

    public static CompletedCourseResponse from(Course course) {
        return CompletedCourseResponse.builder()
                .id(course.getId())
                .title(course.getTitle())
                .description(course.getDescription())
                .region(Objects.requireNonNull(Region.findByCode(course.getRegionId())).getKoreanName())
                .thumbnailUrl(course.getThumbnailUrl())
                .createdAt(course.getCreatedAt())
                .build();
    }
}