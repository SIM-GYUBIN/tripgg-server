package com.ssafy.tripgg.domain.course.dto.response;

import com.ssafy.tripgg.domain.course.entity.Course;
import com.ssafy.tripgg.domain.course.entity.enums.Region;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.util.Objects;

@Getter
@SuperBuilder
public class NotStartCourseResponse extends BaseCourseResponse {

    public static NotStartCourseResponse from(Course course) {
        return NotStartCourseResponse.builder()
                .id(course.getId())
                .title(course.getTitle())
                .description(course.getDescription())
                .region(Objects.requireNonNull(Region.findByCode(course.getRegionId())).getKoreanName())
                .thumbnailUrl(course.getThumbnailUrl())
                .createdAt(course.getCreatedAt())
                .build();
    }
}
