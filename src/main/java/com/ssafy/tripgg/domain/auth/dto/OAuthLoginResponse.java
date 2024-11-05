package com.ssafy.tripgg.domain.auth.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OAuthLoginResponse {
    private String accessToken;
    private UserInfo user;

    @Getter
    @Builder
    public static class UserInfo {
        private String id;
        private String nickname;
        private boolean isNew;
    }
}
