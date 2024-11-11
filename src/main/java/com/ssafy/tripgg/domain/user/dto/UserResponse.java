package com.ssafy.tripgg.domain.user.dto;

import com.ssafy.tripgg.domain.user.entity.User;
import lombok.*;

@Getter
@Builder
public class UserResponse {
    private Long id;
    private String nickname;
    private String provider;
    private String providerId;
    private String profileImageUrl;
    private Integer totalPoints;

    public static UserResponse from(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .nickname(user.getNickname())
                .provider(user.getProvider().getName())
                .providerId(user.getProviderId())
                .profileImageUrl(user.getProfileImageUrl())
                .totalPoints(user.getTotalPoints())
                .build();
    }
}
