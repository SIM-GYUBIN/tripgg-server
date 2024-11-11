package com.ssafy.tripgg.domain.course.dto;

import com.ssafy.tripgg.domain.course.entity.enums.OrderType;
import com.ssafy.tripgg.domain.course.entity.enums.Region;
import jakarta.validation.constraints.Positive;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class CourseRequest {

    private OrderType order = OrderType.LATEST;

    private Region region = Region.ALL;

    @Positive
    private Integer page = 1;

    private Integer size = 10;
}
