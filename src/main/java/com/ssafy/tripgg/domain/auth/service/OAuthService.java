package com.ssafy.tripgg.domain.auth.service;

import com.ssafy.tripgg.domain.auth.dto.OAuthLoginResponse;
import com.ssafy.tripgg.domain.user.entity.Provider;
import com.ssafy.tripgg.domain.user.entity.User;
import com.ssafy.tripgg.domain.user.repository.UserRepository;
import com.ssafy.tripgg.global.config.jwt.JwtTokenProvider;
import com.ssafy.tripgg.infra.oauth.client.KakaoOAuthClient;
import com.ssafy.tripgg.infra.oauth.dto.KakaoTokenResponse;
import com.ssafy.tripgg.infra.oauth.dto.KakaoUserResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class OAuthService {
    private final KakaoOAuthClient kakaoOAuthClient;
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public OAuthLoginResponse handleKakaoLogin(String code) {
        // 1. 카카오 액세스 토큰 받기
        KakaoTokenResponse tokenResponse = kakaoOAuthClient.getToken(code);

        // 2. 카카오 사용자 정보 받기
//        KakaoUserResponse userResponse = kakaoOAuthClient.getUserInfo(tokenResponse.getAccess_token());
        KakaoUserResponse userResponse = kakaoOAuthClient.getUserInfo(tokenResponse.getAccess_token());

        // 3. 사용자 확인 또는 생성
        Optional<User> existingUser = userRepository.findByProviderAndProviderId(
                Provider.KAKAO,
                userResponse.getId().toString()
        );

        boolean isNew = false;
        User user;

        if (existingUser.isPresent()) {
            user = existingUser.get();
        } else {
            isNew = true;
            user = createKakaoUser(userResponse);
        }

        // 4. JWT 토큰 생성
        String accessToken = jwtTokenProvider.createToken(user.getId().toString());

        // 5. 응답 생성
        return OAuthLoginResponse.builder()
                .accessToken(accessToken)
                .user(OAuthLoginResponse.UserInfo.builder()
                        .id(user.getId().toString())
                        .nickname(user.getNickname())
                        .isNew(isNew)
                        .build())
                .build();
    }

    // 신규 회원 db 저장
    private User createKakaoUser(KakaoUserResponse response) {
        return userRepository.save(User.builder()
                .nickname(response.getKakaoAccount().getProfile().getNickname())
                .provider(Provider.KAKAO)
                .providerId(response.getId().toString())
                .profileImageUrl(response.getKakaoAccount().getProfile().getProfileImageUrl())
                .build());
    }
}