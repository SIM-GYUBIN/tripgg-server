package com.ssafy.tripgg.domain.course.dto;

import com.ssafy.tripgg.domain.course.entity.enums.OrderType;
import com.ssafy.tripgg.domain.course.entity.enums.Region;
import jakarta.validation.constraints.Positive;
import lombok.*;

@Getter @ToString
@Builder @Setter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class CourseRequest {

    private OrderType order = OrderType.LATEST;

    private Region region = Region.ALL;
}
