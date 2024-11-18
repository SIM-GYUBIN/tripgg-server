package com.ssafy.tripgg.domain.auth.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OAuthLoginResult {
    private String accessToken;
    private UserInfoResponse user;

    public static OAuthLoginResult of(String accessToken, UserInfoResponse userInfoResponse) {
        return OAuthLoginResult.builder()
                .accessToken(accessToken)
                .user(userInfoResponse)
                .build();
    }
}
