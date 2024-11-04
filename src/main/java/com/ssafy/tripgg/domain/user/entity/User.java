package com.ssafy.tripgg.domain.user.entity;

import com.ssafy.tripgg.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {

    @Column(nullable = false, length = 50)
    private String nickname;

    @Column
    private String email;

    @Column(nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private Provider provider;    // enum으로 변경

    @Column(name = "provider_id", nullable = false)
    private String providerId;

    @Column(name = "profile_image_url")
    private String profileImageUrl;

    @Column(name = "total_points")
    private Integer totalPoints;

    @Builder
    public User(String nickname, String email, Provider provider, String providerId,
                String profileImageUrl) {
        this.nickname = nickname;
        this.email = email;
        this.provider = provider;
        this.providerId = providerId;
        this.profileImageUrl = profileImageUrl;
        this.totalPoints = 0;    // 초기값 0으로 설정
    }

    public void addPoints(int points) {
        this.totalPoints += points;
    }
}