package com.ssafy.tripgg.domain.auth.controller;

import com.ssafy.tripgg.domain.auth.dto.OAuthLoginRequest;
import com.ssafy.tripgg.domain.auth.dto.OAuthLoginResponse;
import com.ssafy.tripgg.domain.auth.service.OAuthService;
import com.ssafy.tripgg.global.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;

@RestController
@RequestMapping("/api/oauth")
@RequiredArgsConstructor
public class OAuthController {
    private final OAuthService oAuthService;

    @PostMapping("/kakao")
    public ResponseEntity<ApiResponse<OAuthLoginResponse.UserInfo>> kakaoLogin(@RequestBody OAuthLoginRequest request) {

        OAuthLoginResponse loginResponse = oAuthService.handleKakaoLogin(request.getCode());

        // accessToken을 쿠키에 설정
        ResponseCookie cookie = ResponseCookie.from("accessToken", loginResponse.getAccessToken())
                .httpOnly(true)
                .secure(true)
                .sameSite("Strict")
                .path("/")
                .maxAge(Duration.ofDays(7))
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(ApiResponse.success(loginResponse.getUser()));
    }
}