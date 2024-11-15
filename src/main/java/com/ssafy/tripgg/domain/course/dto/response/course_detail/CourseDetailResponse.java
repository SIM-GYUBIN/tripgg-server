package com.ssafy.tripgg.domain.course.dto.response.course_detail;

import com.ssafy.tripgg.domain.course.dto.query.CourseDetailQuery;
import com.ssafy.tripgg.domain.course.entity.enums.ProgressStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class CourseDetailResponse {
    private Long id;
    private String title;
    private String description;
    private String region;
    private ProgressStatus status;
    private List<PlaceResponse> places;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static CourseDetailResponse from(CourseDetailQuery query) {
        return CourseDetailResponse.builder()
                .id(query.getId())
                .title(query.getTitle())
                .description(query.getDescription())
                .region(query.getRegion())
                .status(query.getStatus())
                .places(query.getPlaces())
                .createdAt(query.getCreatedAt())
                .updatedAt(query.getUpdatedAt())
                .build();
    }
}
