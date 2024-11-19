package com.ssafy.tripgg.global.common.util;

import lombok.Getter;
import org.springframework.core.io.InputStreamResource;

import java.io.InputStream;

@Getter
public class MultipartInputStreamFileResource extends InputStreamResource {
    private final String filename;

    public MultipartInputStreamFileResource(InputStream inputStream, String filename) {
        super(inputStream);
        this.filename = filename;
    }

    @Override
    public String getFilename() {
        return this.filename;
    }

    @Override
    public long contentLength() {
        return -1; // 길이를 알 수 없음을 나타냄
    }
}
