package com.ssafy.tripgg.domain.verification.service;

import com.ssafy.tripgg.domain.course.entity.CourseProgress;
import com.ssafy.tripgg.domain.course.repository.CourseProgressRepository;
import com.ssafy.tripgg.domain.place.entity.Place;
import com.ssafy.tripgg.domain.place.repository.PlaceRepository;
import com.ssafy.tripgg.domain.verification.dto.ApickApiResponse;
import com.ssafy.tripgg.domain.verification.dto.LocationRequest;
import com.ssafy.tripgg.domain.verification.entity.PlaceVerification;
import com.ssafy.tripgg.domain.verification.repository.PlaceVerificationRepository;
import com.ssafy.tripgg.global.common.util.ImageUtils;
import com.ssafy.tripgg.global.error.ErrorCode;
import com.ssafy.tripgg.global.error.exception.BusinessException;
import com.ssafy.tripgg.infra.apick.ImageSimilarityApiClient;
import com.ssafy.tripgg.infra.aws.S3ObjectStorage;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class VerificationService {
    private static final double MAX_DISTANCE = 100.0; //100m
    private static final int EARTH_RADIUS = 6371000;
    public static final double SIMILARITY_THRESHOLD = 0.8;

    private final PlaceVerificationRepository placeVerificationRepository;
    private final PlaceRepository placeRepository;
    private final CourseProgressRepository courseProgressRepository;

    private final S3ObjectStorage s3ObjectStorage;
    private final ImageUtils imageUtils;
    private final ImageSimilarityApiClient imageSimilarityApiClient;

    public void verifyGps(Long userId, Long courseId, Long placeId, LocationRequest userLocation) {

        // courseid와 userid로 course_progress 가져온다.
        Place place = placeRepository.findById(placeId)  // existsById로 변경
                .orElseThrow(() -> new BusinessException(ErrorCode.PLACE_NOT_FOUND));

        // 인증 처리
        CourseProgress courseProgress = courseProgressRepository.findByUser_IdAndCourse_Id(userId, courseId)
                .orElseThrow(() -> new BusinessException(ErrorCode.COURSE_PROCESS_NOT_FOUND));

        // 중복 검증 방지
        if (placeVerificationRepository.existsByCourseProgress_IdAndPlace_Id(courseProgress.getId(), placeId)) {
            throw new BusinessException(ErrorCode.ALREADY_GPS_VERIFIED);
        }

        // place의 위도, 경도와 location의 위도, 경도를 비교하여 일정 범위 내에 있는지 확인한다.
        validateLocation(place, userLocation);

        PlaceVerification verification = PlaceVerification.builder()
                .courseProgress(courseProgress)
                .place(place)
                .verifiedLatitude(userLocation.getLatitude())
                .verifiedLongitude(userLocation.getLongitude())
                .build();

        placeVerificationRepository.save(verification);
    }


    private void validateLocation(Place place, LocationRequest userLocation) {
        double distance = calculateDistance(
                place.getLatitude().doubleValue(),
                place.getLongitude().doubleValue(),
                userLocation.getLatitude().doubleValue(),
                userLocation.getLongitude().doubleValue());

        if (distance > MAX_DISTANCE) {  // 50미터
            throw new BusinessException(ErrorCode.INVALID_LOCATION);
        }
    }

    public static double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) *
                        Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return EARTH_RADIUS * c;  // 미터 단위로 반환
    }

    public void verifyImage(Long userId, Long courseId, Long placeId, MultipartFile userImage) {
        Place place = placeRepository.findById(placeId)
                .orElseThrow(() -> new BusinessException(ErrorCode.PLACE_NOT_FOUND));

        CourseProgress courseProgress = courseProgressRepository.findByUser_IdAndCourse_Id(userId, courseId)
                .orElseThrow(() -> new BusinessException(ErrorCode.COURSE_PROCESS_NOT_FOUND));

        PlaceVerification verification = placeVerificationRepository.findByCourseProgress_IdAndPlace_Id(courseProgress.getId(), placeId)
                .orElseThrow(() -> new BusinessException(ErrorCode.PLACE_VERIFICATION_NOT_FOUND));

        if (verification.getPhotoVerified()) {
            throw new BusinessException(ErrorCode.ALREADY_PHOTO_VERIFIED);
        }

        try {

            MultipartFile baseImage = imageUtils.urlToMultipartFile(place.getImageUrl());

            ApickApiResponse response = imageSimilarityApiClient.compareImages(baseImage, List.of(userImage));

            log.info("similarity: {}", response.getData().getOutput().get("compare_image1"));

            if (response.getData().getSuccess() == 1 && response.getData().getOutput().get("compare_image1") >= SIMILARITY_THRESHOLD) {
                String imageUrl = s3ObjectStorage.uploadFile(userImage);
                verification.photoVerify(imageUrl);
            } else {
                throw new BusinessException(ErrorCode.PHOTO_VERIFY_FAILED);
            }

        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.VERIFICATION_FAILED);
        }
    }
}
