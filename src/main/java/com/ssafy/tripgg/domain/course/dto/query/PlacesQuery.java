package com.ssafy.tripgg.domain.course.dto.query;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class PlacesQuery {
    private String name;
    private String description;
}
