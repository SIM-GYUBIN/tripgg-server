package com.ssafy.tripgg.domain.course.dto.response.course_detail;

import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;

@Value
@Builder
public class PlaceResponse {
    Long id;
    String name;
    String description;
    BigDecimal latitude;
    BigDecimal longitude;
    String address;
    String imageUrl;
    Integer sequence;
    boolean isVerified;
    boolean isPhotoVerified;
}
