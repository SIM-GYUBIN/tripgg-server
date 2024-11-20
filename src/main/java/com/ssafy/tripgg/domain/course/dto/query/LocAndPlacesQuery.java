package com.ssafy.tripgg.domain.course.dto.query;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.List;

@Getter @ToString
@Builder
@AllArgsConstructor
public class LocAndPlacesQuery {
    private BigDecimal latitude;
    private BigDecimal longitude;

    private List<PlacesQuery> places;
}
