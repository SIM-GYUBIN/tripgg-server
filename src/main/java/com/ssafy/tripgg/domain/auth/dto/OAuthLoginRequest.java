package com.ssafy.tripgg.domain.auth.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class OAuthLoginRequest {
    private String code;
}
