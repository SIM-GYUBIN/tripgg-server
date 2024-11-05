package com.ssafy.tripgg.domain.auth.controller;

import com.ssafy.tripgg.domain.auth.dto.OAuthLoginRequest;
import com.ssafy.tripgg.domain.auth.dto.OAuthLoginResponse;
import com.ssafy.tripgg.domain.auth.service.OAuthService;
import com.ssafy.tripgg.global.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/oauth")
@RequiredArgsConstructor
public class OAuthController {
    private final OAuthService oAuthService;

    @PostMapping("/kakao")
    public ApiResponse<OAuthLoginResponse> kakaoLogin(@RequestBody OAuthLoginRequest request) {
        return ApiResponse.success(oAuthService.handleKakaoLogin(request.getCode()));
    }
}