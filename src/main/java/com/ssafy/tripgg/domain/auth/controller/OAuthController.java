package com.ssafy.tripgg.domain.auth.controller;

import com.ssafy.tripgg.domain.auth.dto.OAuthLoginRequest;
import com.ssafy.tripgg.domain.auth.dto.OAuthLoginResult;
import com.ssafy.tripgg.domain.auth.dto.UserInfoResponse;
import com.ssafy.tripgg.domain.auth.service.OAuthService;
import com.ssafy.tripgg.global.common.ApiResponse;
import com.ssafy.tripgg.global.common.util.CookieUtil;
import com.ssafy.tripgg.global.error.ErrorCode;
import com.ssafy.tripgg.global.error.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api/oauth")
@RequiredArgsConstructor
public class OAuthController {
    private final OAuthService oAuthService;

    @PostMapping("/kakao")
    public ResponseEntity<ApiResponse<UserInfoResponse>> kakaoLogin(@RequestBody OAuthLoginRequest request) {

        OAuthLoginResult loginResult = null;
        try {
            loginResult = oAuthService.handleKakaoLogin(request.getCode());
        } catch (IOException e) {
            throw new BusinessException(ErrorCode.OAUTH_SERVER_ERROR);
        }

        // accessToken을 쿠키에 설정
        ResponseCookie cookie = CookieUtil.createAccessTokenCookie(loginResult.getAccessToken());

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(ApiResponse.success(loginResult.getUser()));
    }
}