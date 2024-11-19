package com.ssafy.tripgg.domain.auth.service;

import com.ssafy.tripgg.domain.auth.dto.OAuthLoginResult;
import com.ssafy.tripgg.domain.auth.dto.UserInfoResponse;
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

import java.io.IOException;
import java.util.Optional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class OAuthService {
    private final KakaoOAuthClient kakaoOAuthClient;
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public OAuthLoginResult handleKakaoLogin(String code) throws IOException {

        KakaoTokenResponse tokenResponse = kakaoOAuthClient.getToken(code);
        KakaoUserResponse userResponse = kakaoOAuthClient.getUserInfo(tokenResponse.getAccess_token());

        Optional<User> existingUser = userRepository.findByProviderAndProviderId(
                Provider.KAKAO,
                userResponse.getId().toString()
        );

        boolean isNew = existingUser.isEmpty();
        User user = existingUser.orElseGet(() -> createKakaoUser(userResponse));

        String accessToken = jwtTokenProvider.createToken(user.getId().toString());
        UserInfoResponse userInfoResponse = UserInfoResponse.of(user, isNew);

        return OAuthLoginResult.of(accessToken, userInfoResponse);
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