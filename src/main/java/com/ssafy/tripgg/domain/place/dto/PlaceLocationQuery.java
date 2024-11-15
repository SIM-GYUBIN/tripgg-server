package com.ssafy.tripgg.domain.place.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class PlaceLocationQuery {
    private Long id;
    private BigDecimal latitude;
    private BigDecimal longitude;
}
