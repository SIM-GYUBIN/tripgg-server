package com.ssafy.tripgg.domain.auth.dto;

import com.ssafy.tripgg.domain.user.entity.User;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserInfoResponse {
        private String id;
        private String nickname;
        private String profileImageUrl;
        private boolean isNew;

        public static UserInfoResponse of(User user, boolean isNew) {
                return UserInfoResponse.builder()
                        .id(user.getId().toString())
                        .nickname(user.getNickname())
                        .profileImageUrl(user.getProfileImageUrl())
                        .isNew(isNew)
                        .build();
        }
}
