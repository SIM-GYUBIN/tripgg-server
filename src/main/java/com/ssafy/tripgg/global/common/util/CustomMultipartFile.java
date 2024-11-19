package com.ssafy.tripgg.global.common.util;

import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

@Getter
public class CustomMultipartFile implements MultipartFile {
    private final byte[] content;
    private final String name;
    private final String contentType;

    public CustomMultipartFile(byte[] content, String name, String contentType) {
        this.content = content;
        this.name = name;
        this.contentType = contentType;
    }

    @Override
    public String getOriginalFilename() {
        return name;
    }

    @Override
    public boolean isEmpty() {
        return content == null || content.length == 0;
    }

    @Override
    public long getSize() {
        return content.length;
    }

    @Override
    public byte[] getBytes() {
        return content;
    }

    @Override
    public InputStream getInputStream() {
        return new ByteArrayInputStream(content);
    }

    @Override
    public void transferTo(File dest) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(dest)) {
            fos.write(content);
        }
    }
}
