package com.ssafy.tripgg.domain.course.dto;

import com.ssafy.tripgg.domain.course.entity.enums.OrderType;
import com.ssafy.tripgg.domain.course.entity.enums.Region;
import com.ssafy.tripgg.global.common.validator.ValidEnum;
import lombok.Data;

@Data
public class CourseRequest {

    @ValidEnum(enumClass = OrderType.class)
    private OrderType orderBy = OrderType.LATEST;

    @ValidEnum(enumClass = Region.class)
    private Region region = Region.ALL;
}
