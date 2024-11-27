package com.ssafy.tripgg.domain.verification.service;

import com.ssafy.tripgg.domain.course.entity.CourseProgress;
import com.ssafy.tripgg.domain.course.repository.CourseProgressRepository;
import com.ssafy.tripgg.domain.place.entity.Place;
import com.ssafy.tripgg.domain.place.repository.PlaceRepository;
import com.ssafy.tripgg.domain.verification.dto.ApickApiResponse;
import com.ssafy.tripgg.domain.verification.dto.LocationRequest;
import com.ssafy.tripgg.domain.verification.entity.PlaceVerification;
import com.ssafy.tripgg.domain.verification.repository.PlaceVerificationRepository;
import com.ssafy.tripgg.global.common.util.GeoUtils;
import com.ssafy.tripgg.global.common.util.ImageUtils;
import com.ssafy.tripgg.global.error.ErrorCode;
import com.ssafy.tripgg.global.error.exception.BusinessException;
import com.ssafy.tripgg.infra.apick.ImageSimilarityApiClient;
import com.ssafy.tripgg.domain.verification.constants.VerificationConstants;
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

        validateLocationInRange(place, userLocation);

        PlaceVerification verification = PlaceVerification.builder()
                .courseProgress(courseProgress)
                .place(place)
                .verifiedLatitude(userLocation.getLatitude())
                .verifiedLongitude(userLocation.getLongitude())
                .build();

        placeVerificationRepository.save(verification);
    }

    private void validateLocationInRange(Place place, LocationRequest userLocation) {
        double distance = GeoUtils.calculateDistance(
                place.getLatitude().doubleValue(),
                place.getLongitude().doubleValue(),
                userLocation.getLatitude().doubleValue(),
                userLocation.getLongitude().doubleValue());

        if (distance > VerificationConstants.MAX_DISTANCE) {
            throw new BusinessException(ErrorCode.INVALID_LOCATION);
        }
    }


    public String verifyImage(Long userId, Long courseId, Long placeId, MultipartFile userImage) {
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

            Double similarity = response.getData().getOutput().get("compare_image1");

            log.info("UserId: {}", userId);
            log.info("PlaceId: {}", placeId);
            log.info("Similarity: {}", similarity);

            if (response.getData().getSuccess() == 1 && similarity >= VerificationConstants.SIMILARITY_THRESHOLD) {
                String imageUrl = s3ObjectStorage.uploadFile(userImage);
                verification.photoVerify(imageUrl);
            } else {
                throw new BusinessException(ErrorCode.PHOTO_VERIFY_FAILED);
            }

            return similarity.toString();

        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.VERIFICATION_FAILED);
        }
    }
}
