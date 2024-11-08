package com.ssafy.tripgg.domain.course.dto;

import com.ssafy.tripgg.domain.course.entity.enums.Region;
import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class CourseRequest {
    private String order = "asc";
    private Region region = Region.ALL;
    private Integer page = 1;
}
