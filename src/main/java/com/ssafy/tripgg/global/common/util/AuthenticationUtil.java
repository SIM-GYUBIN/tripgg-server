package com.ssafy.tripgg.global.common.util;

import com.ssafy.tripgg.global.config.security.UserPrincipal;
import com.ssafy.tripgg.global.error.exception.BusinessException;
import com.ssafy.tripgg.global.error.ErrorCode;

public class AuthenticationUtil {
    public static Long getCurrentUserId(UserPrincipal userPrincipal) {
        if (userPrincipal == null) {
            throw new BusinessException(ErrorCode.USER_NOT_FOUND);
        }
        return userPrincipal.getId();
    }

    public static Long getCurrentUserIdCanNull(UserPrincipal userPrincipal) {
        return userPrincipal == null ? null : userPrincipal.getId();
    }
}
