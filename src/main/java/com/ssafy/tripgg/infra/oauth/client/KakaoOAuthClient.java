package com.ssafy.tripgg.infra.oauth.client;

import com.ssafy.tripgg.infra.oauth.dto.KakaoTokenResponse;
import com.ssafy.tripgg.infra.oauth.dto.KakaoUserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class KakaoOAuthClient {
    private final RestTemplate restTemplate;

    @Value("${oauth.kakao.client-id}")
    private String clientId;

    @Value("${oauth.kakao.redirect-uri}")
    private String redirectUri;

    public KakaoTokenResponse getToken(String code) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add(KakaoOAuthConstants.Parameters.GRANT_TYPE, KakaoOAuthConstants.GrantTypes.AUTHORIZATION_CODE);
        params.add(KakaoOAuthConstants.Parameters.CLIENT_ID, clientId);
        params.add(KakaoOAuthConstants.Parameters.REDIRECT_URI, redirectUri);
        params.add(KakaoOAuthConstants.Parameters.CODE, code);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

        return restTemplate.postForObject(KakaoOAuthConstants.Urls.TOKEN, request, KakaoTokenResponse.class);
    }

    public KakaoUserResponse getUserInfo(String accessToken) {

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        HttpEntity<?> request = new HttpEntity<>(headers);

        return restTemplate.exchange(
                KakaoOAuthConstants.Urls.USER_INFO,
                HttpMethod.GET,
                request,
                KakaoUserResponse.class
        ).getBody();
    }
}
