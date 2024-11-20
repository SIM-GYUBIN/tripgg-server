package com.ssafy.tripgg.domain.course.dto.response.course_detail;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CourseFinishResponse {
    private Integer gainedScore;
}
