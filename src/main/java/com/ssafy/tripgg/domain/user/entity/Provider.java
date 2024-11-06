package com.ssafy.tripgg.domain.user.entity;

public enum Provider {
    KAKAO, NAVER;

    public String getName() {
        return this.name().toLowerCase();
    }
}
