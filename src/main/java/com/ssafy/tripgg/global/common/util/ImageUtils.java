package com.ssafy.tripgg.global.common.util;

import com.ssafy.tripgg.global.error.ErrorCode;
import com.ssafy.tripgg.global.error.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class ImageUtils {

    private final OkHttpClient okHttpClient;

    public MultipartFile urlToMultipartFile(String imageUrl) {
        try {
            Request request = new Request.Builder()
                    .url(imageUrl)
                    .get()
                    .build();

            try (Response response = okHttpClient.newCall(request).execute()) {
                if (!response.isSuccessful()) {
                    throw new BusinessException(ErrorCode.IMAGE_DOWNLOAD_FAILED);
                }

                assert response.body() != null;
                byte[] imageBytes = response.body().bytes();
                String fileName = extractFileName(imageUrl);
                String contentType = determineContentType(fileName);

                return new CustomMultipartFile(imageBytes, fileName, contentType);
            }
        } catch (IOException e) {
            throw new BusinessException(ErrorCode.IMAGE_CONVERSION_FAILED);
        }
    }

    private String extractFileName(String imageUrl) {
        return imageUrl.substring(imageUrl.lastIndexOf("/") + 1);
    }

    private String determineContentType(String fileName) {
        String extension = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
        return switch (extension) {
            case "png" -> "image/png";
            case "jpg", "jpeg" -> "image/jpeg";
            case "gif" -> "image/gif";
            default -> "application/octet-stream";
        };
    }
}
