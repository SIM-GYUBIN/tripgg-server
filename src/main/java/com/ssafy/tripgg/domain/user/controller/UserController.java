package com.ssafy.tripgg.domain.user.controller;

import com.ssafy.tripgg.domain.user.dto.UserResponse;
import com.ssafy.tripgg.domain.user.service.UserService;
import com.ssafy.tripgg.global.common.ApiResponse;
import com.ssafy.tripgg.global.config.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/me")
    public ApiResponse<UserResponse> getUserInfo(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        Long userId = userPrincipal.getId();

        return ApiResponse.success(userService.findById(userId));
    }
}