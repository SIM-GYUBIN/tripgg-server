package com.ssafy.tripgg.domain.user.entity;

import com.ssafy.tripgg.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE users SET deleted_at = NOW() WHERE id = ?")
@SQLRestriction("deleted_at IS NULL")
public class User extends BaseEntity {

    @Column(nullable = false, length = 50)
    private String nickname;

    @Column(nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private Provider provider;

    @Column(name = "provider_id", nullable = false)
    private String providerId;

    @Column(name = "profile_image_url")
    private String profileImageUrl;

    @Column(name = "total_points")
    private Integer totalPoints;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Builder
    public User(String nickname, Provider provider, String providerId,
                String profileImageUrl) {
        this.nickname = nickname;
        this.provider = provider;
        this.providerId = providerId;
        this.profileImageUrl = profileImageUrl;
        this.totalPoints = 0;    // 초기값 0으로 설정
    }

    public void addPoints(int points) {
        this.totalPoints += points;
    }

    public void delete() {
        this.deletedAt = LocalDateTime.now();
    }
}