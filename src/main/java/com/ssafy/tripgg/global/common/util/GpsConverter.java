package com.ssafy.tripgg.global.common.util;

import org.springframework.stereotype.Component;

@Component
public class GpsConverter {
    // 격자 <-> 위경도 변환 상수
    private static final double RE = 6371.00877; // 지구 반경(km)
    private static final double GRID = 5.0; // 격자 간격(km)
    private static final double SLAT1 = 30.0; // 투영 위도1(degree)
    private static final double SLAT2 = 60.0; // 투영 위도2(degree)
    private static final double OLON = 126.0; // 기준점 경도(degree)
    private static final double OLAT = 38.0; // 기준점 위도(degree)
    private static final double XO = 43; // 기준점 X좌표(GRID)
    private static final double YO = 136; // 기준점 Y좌표(GRID)

    private static final int TO_GRID = 0;
    private static final int TO_GPS = 1;

    /**
     * 위경도 좌표를 기상청 격자 좌표로 변환
     * @param lat 위도
     * @param lng 경도
     * @return 격자 좌표 (x, y)
     */
    public LatXLngY convertToGrid(double lat, double lng) {
        return convertGRID_GPS(TO_GRID, lat, lng);
    }

    /**
     * 기상청 격자 좌표를 위경도 좌표로 변환
     * @param x 격자 X좌표
     * @param y 격자 Y좌표
     * @return 위경도 좌표 (lat, lng)
     */
    public LatXLngY convertToGPS(double x, double y) {
        return convertGRID_GPS(TO_GPS, x, y);
    }

    public LatXLngY convertGRID_GPS(int mode, double lat_X, double lng_Y) {
        double DEGRAD = Math.PI / 180.0;
        double RADDEG = 180.0 / Math.PI;

        double re = RE / GRID;
        double slat1 = SLAT1 * DEGRAD;
        double slat2 = SLAT2 * DEGRAD;
        double olon = OLON * DEGRAD;
        double olat = OLAT * DEGRAD;

        double sn = Math.tan(Math.PI * 0.25 + slat2 * 0.5) / Math.tan(Math.PI * 0.25 + slat1 * 0.5);
        sn = Math.log(Math.cos(slat1) / Math.cos(slat2)) / Math.log(sn);
        double sf = Math.tan(Math.PI * 0.25 + slat1 * 0.5);
        sf = Math.pow(sf, sn) * Math.cos(slat1) / sn;
        double ro = Math.tan(Math.PI * 0.25 + olat * 0.5);
        ro = re * sf / Math.pow(ro, sn);
        LatXLngY rs = new LatXLngY();

        if (mode == TO_GRID) {
            rs.setLat(lat_X);
            rs.setLng(lng_Y);
            double ra = Math.tan(Math.PI * 0.25 + (lat_X) * DEGRAD * 0.5);
            ra = re * sf / Math.pow(ra, sn);
            double theta = lng_Y * DEGRAD - olon;
            if (theta > Math.PI) theta -= 2.0 * Math.PI;
            if (theta < -Math.PI) theta += 2.0 * Math.PI;
            theta *= sn;
            rs.setX(Math.floor(ra * Math.sin(theta) + XO + 0.5));
            rs.setY(Math.floor(ro - ra * Math.cos(theta) + YO + 0.5));
        } else {
            rs.setX(lat_X);
            rs.setY(lng_Y);
            double xn = lat_X - XO;
            double yn = ro - lng_Y + YO;
            double ra = Math.sqrt(xn * xn + yn * yn);
            if (sn < 0.0) {
                ra = -ra;
            }
            double alat = Math.pow((re * sf / ra), (1.0 / sn));
            alat = 2.0 * Math.atan(alat) - Math.PI * 0.5;

            double theta = 0.0;
            if (Math.abs(xn) <= 0.0) {
                theta = 0.0;
            } else {
                if (Math.abs(yn) <= 0.0) {
                    theta = Math.PI * 0.5;
                    if (xn < 0.0) {
                        theta = -theta;
                    }
                } else {
                    theta = Math.atan2(xn, yn);
                }
            }
            double alon = theta / sn + olon;
            rs.setLat(alat * RADDEG);
            rs.setLng(alon * RADDEG);
        }
        return rs;
    }
}