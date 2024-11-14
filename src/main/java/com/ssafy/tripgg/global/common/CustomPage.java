package com.ssafy.tripgg.global.common;

import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
public class CustomPage<T> {
    private List<T> content;
    private int pageNumber;
    private int totalPages;
    private long totalElements;
    private int pageSize;
    private boolean first;
    private boolean last;

    public CustomPage(Page<T> page) {
        this.content = page.getContent();
        this.pageNumber = page.getNumber() + 1;  // 0-> 1 based
        this.totalPages = page.getTotalPages();
        this.totalElements = page.getTotalElements();
        this.pageSize = page.getSize();
        this.first = page.isFirst();
        this.last = page.isLast();
    }
}

