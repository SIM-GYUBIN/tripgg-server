package com.ssafy.tripgg.domain.verification.controller;

import com.ssafy.tripgg.domain.verification.dto.LocationRequest;
import com.ssafy.tripgg.domain.verification.service.VerificationService;
import com.ssafy.tripgg.global.common.ApiResponse;
import com.ssafy.tripgg.global.common.util.AuthenticationUtil;
import com.ssafy.tripgg.global.config.security.UserPrincipal;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/verify")
@RequiredArgsConstructor
public class VerificationController {

    private final VerificationService verificationService;

    @PostMapping("/gps/courses/{courseId}/places/{placeId}")
    public ApiResponse<String> verifyGps(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable Long courseId,
            @PathVariable Long placeId,
            @RequestBody @Valid LocationRequest location) {

        Long userId = AuthenticationUtil.getCurrentUserId(userPrincipal);

        verificationService.verifyGps(userId, courseId, placeId, location);

        return ApiResponse.success("GPS 인증 성공");
    }

    @PostMapping("/photo/courses/{courseId}/places/{placeId}")
    public ApiResponse<String> verifyImage(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable Long courseId,
            @PathVariable Long placeId,
            @RequestParam("image") MultipartFile image) {

        Long userId = AuthenticationUtil.getCurrentUserId(userPrincipal);
        verificationService.verifyImage(userId, courseId, placeId, image);
        return ApiResponse.success("이미지 인증 성공");
    }
}
