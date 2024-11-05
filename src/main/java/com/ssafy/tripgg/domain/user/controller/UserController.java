package com.ssafy.tripgg.domain.user.controller;

import com.ssafy.tripgg.domain.user.service.UserService;
import com.ssafy.tripgg.global.common.ApiResponse;
import com.ssafy.tripgg.global.config.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/info")
    public ApiResponse getUserInfo(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        Long userId = userPrincipal.getId();
        // userId를 이용한 로직 구현
        return ApiResponse.success(userService.findById(userId));
    }
}