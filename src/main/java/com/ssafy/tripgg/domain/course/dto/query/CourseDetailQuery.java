package com.ssafy.tripgg.domain.course.dto.query;

import com.ssafy.tripgg.domain.course.dto.response.course_detail.PlaceResponse;
import com.ssafy.tripgg.domain.course.entity.enums.ProgressStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data @Builder
@AllArgsConstructor
public class CourseDetailQuery {
    private Long id;
    private String title;
    private String description;
    private String region;
    private ProgressStatus status;

    private Long totalPlaceNum;
    private Long verifiedPlaceNum;

    private List<PlaceResponse> places;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
