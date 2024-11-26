package com.ssafy.tripgg.global.common.util;

import org.springframework.http.ResponseCookie;

import java.time.Duration;

public class CookieUtil {
    private static final String TOKEN_NAME = "accessToken";

    public static ResponseCookie createAccessTokenCookie(String token) {

        return ResponseCookie.from(TOKEN_NAME, token)
                .httpOnly(false)
                .secure(true)
                .sameSite("None")
                .path("/")
                .maxAge(Duration.ofDays(7))
                .build();
    }
}
