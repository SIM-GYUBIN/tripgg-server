package com.ssafy.tripgg.domain.course.dto.response.course_list;

import com.ssafy.tripgg.domain.course.dto.query.InProgressCourseQuery;
import com.ssafy.tripgg.domain.course.entity.enums.Region;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@SuperBuilder
public class InProgressCourseResponse extends BaseCourseResponse{

    private Long totalPlaceNum;
    private Long verifiedPlaceNum;
    private LocalDateTime startedAt;

    public static InProgressCourseResponse of(InProgressCourseQuery queryDto) {
        return InProgressCourseResponse.builder()
                .id(queryDto.getId())
                .title(queryDto.getTitle())
                .description(queryDto.getDescription())
                .region(Objects.requireNonNull(Region.findByCode(queryDto.getRegionId())).getKoreanName())
                .thumbnailUrl(queryDto.getThumbnailUrl())
                .createdAt(queryDto.getCreatedAt())
                .totalPlaceNum(queryDto.getTotalPlaceNum())
                .verifiedPlaceNum(queryDto.getVerifiedPlaceNum())
                .startedAt(queryDto.getStartedAt())
                .build();
    }
}
