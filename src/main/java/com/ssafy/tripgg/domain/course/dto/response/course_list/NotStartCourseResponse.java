package com.ssafy.tripgg.domain.course.dto.response.course_list;

import com.ssafy.tripgg.domain.course.entity.Course;
import com.ssafy.tripgg.domain.course.entity.enums.Region;
import com.ssafy.tripgg.global.common.util.ImageUtils;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.util.Objects;

@Getter
@SuperBuilder
public class NotStartCourseResponse extends BaseCourseResponse {

    public static NotStartCourseResponse of(Course course) {
        return NotStartCourseResponse.builder()
                .id(course.getId())
                .title(course.getTitle())
                .description(course.getDescription())
                .region(Objects.requireNonNull(Region.findByCode(course.getRegionId())).getKoreanName())
                .thumbnailUrl(ImageUtils.checkImageUrl(course.getThumbnailUrl()))
                .createdAt(course.getCreatedAt())
                .build();
    }
}
