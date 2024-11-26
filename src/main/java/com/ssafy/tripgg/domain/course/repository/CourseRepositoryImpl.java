package com.ssafy.tripgg.domain.course.repository;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ssafy.tripgg.domain.course.dto.query.CourseDetailQuery;
import com.ssafy.tripgg.domain.course.dto.query.LocAndPlacesQuery;
import com.ssafy.tripgg.domain.course.dto.query.PlacesQuery;
import com.ssafy.tripgg.domain.course.dto.response.course_detail.PlaceResponse;
import com.ssafy.tripgg.domain.course.entity.QCourse;
import com.ssafy.tripgg.domain.course.entity.QCoursePlace;
import com.ssafy.tripgg.domain.course.entity.QCourseProgress;
import com.ssafy.tripgg.domain.course.entity.enums.ProgressStatus;
import com.ssafy.tripgg.domain.course.entity.enums.Region;
import com.ssafy.tripgg.domain.place.entity.QPlace;
import com.ssafy.tripgg.domain.verification.entity.QPlaceVerification;
import com.ssafy.tripgg.global.common.util.ImageUtils;
import com.ssafy.tripgg.global.error.ErrorCode;
import com.ssafy.tripgg.global.error.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Repository
@RequiredArgsConstructor
public class CourseRepositoryImpl implements CourseCustomRepository {
    private final JPAQueryFactory queryFactory;

    public CourseDetailQuery findCourseDetailById(Long courseId, Long userId) {
        QCourse course = QCourse.course;
        QCoursePlace coursePlace = QCoursePlace.coursePlace;
        QPlace place = QPlace.place;
        QCourseProgress courseProgress = QCourseProgress.courseProgress;
        QPlaceVerification placeVerification = QPlaceVerification.placeVerification;
//        QRegion region = QRegion.regionEntity;

        // 기본 정보와 장소 정보를 한 번에 조회
        List<Tuple> results = queryFactory
                .select(
                        course.id,
                        course.title,
                        course.description,
                        course.regionId,
                        course.createdAt,
                        course.updatedAt,
                        coursePlace.sequence,
                        ExpressionUtils.as(
                                JPAExpressions
                                        .select(coursePlace.count())
                                        .from(coursePlace)
                                        .where(coursePlace.course.id.eq(courseId)),
                                "totalPlaceNum"
                        ),
                        ExpressionUtils.as(
                                JPAExpressions
                                        .select(placeVerification.count())
                                        .from(placeVerification)
                                        .where(placeVerification.courseProgress.course.id.eq(courseId)
                                                .and(placeVerification.courseProgress.user.id.eq(userId))),
                                "verifiedPlaceNum"
                        ),
                        place.id, // 9
                        place.name,
                        place.description,
                        place.latitude,
                        place.longitude,
                        place.address,
                        place.imageUrl,
                        courseProgress.status,
                        placeVerification.id.isNotNull().as("isVerified"), //17
                        placeVerification.photoVerified
                )
                .from(course)
                .leftJoin(coursePlace).on(coursePlace.course.eq(course))
                .leftJoin(place).on(coursePlace.place.eq(place))
                .leftJoin(courseProgress).on(courseProgress.course.eq(course)
                        .and(courseProgress.user.id.eq(userId)))
                .leftJoin(placeVerification).on(placeVerification.courseProgress.eq(courseProgress)
                        .and(placeVerification.place.eq(place)))
                .where(course.id.eq(courseId))
                .orderBy(coursePlace.sequence.asc())
                .fetch();

        if (results.isEmpty()) {
            throw new BusinessException(ErrorCode.COURSE_NOT_FOUND);
        }

        // 첫 번째 row에서 코스 기본 정보 추출
        Tuple firstRow = results.get(0);

        // Region enum으로 변환
        Region region = Objects.requireNonNull(Region.findByCode(firstRow.get(course.regionId)));

        // 장소 정보 매핑
        List<PlaceResponse> places = results.stream()
                .filter(row -> row.get(9, Long.class) != null)  // place.id
                .map(row -> PlaceResponse.builder()
                        .id(row.get(9, Long.class))            // place.id
                        .name(row.get(10, String.class))       // place.name
                        .description(row.get(11, String.class)) // place.description
                        .latitude(row.get(12, BigDecimal.class))   // place.latitude
                        .longitude(row.get(13, BigDecimal.class))  // place.longitude
                        .address(row.get(14, String.class))    // place.address
                        .imageUrl(ImageUtils.checkImageUrl(row.get(15, String.class)))   // place.imageUrl
                        .sequence(row.get(6, Integer.class))   // coursePlace.sequence
                        .canPhotoVerify(StringUtils.hasText(row.get(15, String.class)))// imageUrl이 null인지 확인
                        .isVerified(Boolean.TRUE.equals(row.get(17, Boolean.class)))  // isVerified
                        .isPhotoVerified(Boolean.TRUE.equals(row.get(18, Boolean.class))) // photoVerified
                        .build())
                .toList();

        // CourseDetailQuery 생성
        return CourseDetailQuery.builder()
                .id(firstRow.get(0, Long.class))           // course.id
                .title(firstRow.get(1, String.class))      // course.title
                .description(firstRow.get(2, String.class)) // course.description
                .region(region.getKoreanName())
                .status(firstRow.get(16, ProgressStatus.class))  // courseProgress.status
                .totalPlaceNum(firstRow.get(7, Long.class))     // coursePlace count subquery 결과
                .verifiedPlaceNum(firstRow.get(8, Long.class))  // placeVerification count subquery 결과
                .places(places)
                .createdAt(firstRow.get(4, LocalDateTime.class))  // course.createdAt
                .updatedAt(firstRow.get(5, LocalDateTime.class))  // course.updatedAt
                .build();
    }


    @Override
    public LocAndPlacesQuery findLocAndPlacesByCourseId(Long courseId) {

        QCourse course = QCourse.course;
        QCoursePlace coursePlace = QCoursePlace.coursePlace;
        QPlace place = QPlace.place;

        List<Tuple> results = queryFactory
                .select(
                        place.latitude,
                        place.longitude,
                        place.name,
                        place.description
                )
                .from(course)
                .leftJoin(coursePlace).on(coursePlace.course.eq(course))
                .leftJoin(place).on(coursePlace.place.eq(place))
                .where(course.id.eq(courseId))
                .orderBy(coursePlace.sequence.asc())
                .fetch();

        return LocAndPlacesQuery.builder()
                .latitude(results.get(0).get(0, BigDecimal.class))
                .longitude(results.get(0).get(1, BigDecimal.class))
                .places(results.stream()
                        .map(row -> PlacesQuery.builder()
                                .name(row.get(2, String.class))
                                .description(row.get(3, String.class))
                                .build())
                        .toList())
                .build();
    }
}