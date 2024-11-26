package com.ssafy.tripgg.global.common.util;


import com.ssafy.tripgg.domain.verification.constants.VerificationConstants;
import lombok.experimental.UtilityClass;

@UtilityClass
public class GeoUtils {
    public double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) *
                        Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return VerificationConstants.EARTH_RADIUS * c;  // 미터 단위로 반환
    }
}
