package com.ssafy.tripgg.global.common.util;

import org.springframework.http.ResponseCookie;

import java.time.Duration;

public class CookieUtil {
    private static final String TOKEN_NAME = "accessToken";

    public static ResponseCookie createAccessTokenCookie(String token) {

        return ResponseCookie.from(TOKEN_NAME, token)
                .httpOnly(true)
                .secure(true)
                .sameSite("Strict")
                .path("/")
                .maxAge(Duration.ofDays(7))
                .build();
    }
}
